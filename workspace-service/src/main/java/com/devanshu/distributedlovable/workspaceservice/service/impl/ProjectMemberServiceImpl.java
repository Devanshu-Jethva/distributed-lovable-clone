package com.devanshu.distributedlovable.workspaceservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devanshu.distributedlovable.commonlib.constants.ProjectMemberStatus;
import com.devanshu.distributedlovable.commonlib.constants.ProjectRole;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;
import com.devanshu.distributedlovable.commonlib.dto.UserIdsDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileListDTO;
import com.devanshu.distributedlovable.commonlib.exceptions.BadRequestException;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;
import com.devanshu.distributedlovable.commonlib.exceptions.PermissionException;
import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;
import com.devanshu.distributedlovable.commonlib.service.HelperCommonService;
import com.devanshu.distributedlovable.workspaceservice.client.AccountServiceClient;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.member.InviteProjectMemberRequestDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.member.ProjectMemberDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMember;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMemberId;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;
import com.devanshu.distributedlovable.workspaceservice.exception.AccountServiceExceptionHandler;
import com.devanshu.distributedlovable.workspaceservice.mapper.ProjectMemberMapper;
import com.devanshu.distributedlovable.workspaceservice.repository.ProjectMemberRepository;
import com.devanshu.distributedlovable.workspaceservice.repository.ProjectsRepository;
import com.devanshu.distributedlovable.workspaceservice.service.ProjectMemberService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProjectMemberServiceImpl implements ProjectMemberService {

	ProjectMemberRepository projectMemberRepository;
	ProjectsRepository projectsRepository;
	ProjectMemberMapper projectMemberMapper;
	HelperCommonService helperCommonService;
	AccountServiceClient accountServiceClient;
	AccountServiceExceptionHandler accountServiceExceptionHandler;

	@Override
	@Transactional(readOnly = true)
	public List<ProjectMemberDTO> getProjectMembers(final Long projectId) {

		List<ProjectMember> projectMembers = projectMemberRepository.findById_ProjectId(projectId);

		if (projectMembers.isEmpty()) {
			return List.of();
		}

		List<Long> userIds = projectMembers.stream().map(member -> member.getId().getUserId()).distinct().toList();

		ResponseEntity<ApiResponse<UserProfileListDTO>> response = accountServiceClient
				.getUsersByIds(new UserIdsDTO(userIds));

		List<UserProfileDTO> users = Objects.requireNonNull(response.getBody()).getData().userProfileDTOs();

		Map<Long, UserProfileDTO> userMap = users.stream()
				.collect(Collectors.toMap(UserProfileDTO::id, Function.identity()));

		return projectMembers.stream().map(member -> {
			UserProfileDTO user = userMap.get(member.getId().getUserId());

			if (user == null) {
				throw new NotFoundException("User not found with id: " + member.getId().getUserId());
			}

			return projectMemberMapper.toDto(member, user);
		}).toList();
	}

	@Override
	public ProjectMemberDTO inviteProjectMember(final Long projectId,
			final InviteProjectMemberRequestDTO inviteProjectMemberRequestDTO) {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		if (inviteProjectMemberRequestDTO.username().equals(jwtUserPrincipal.username())) {
			throw new BadRequestException("User cannot invite themselves");
		}

		UserProfileDTO invitedUser = null;
		try {
			ResponseEntity<ApiResponse<UserProfileDTO>> response = accountServiceClient
					.getUserByEmail(inviteProjectMemberRequestDTO.username());

			invitedUser = response.getBody().getData();

		} catch (FeignException ex) {
			throw accountServiceExceptionHandler.handle(ex);
		}
		if (invitedUser.deletedAt() != null) {
			throw new BadRequestException(
					"User with username " + inviteProjectMemberRequestDTO.username() + " is inactive");
		}
		if (projectMemberRepository.existsById_ProjectIdAndId_UserId(projectId, invitedUser.id())) {
			throw new BadRequestException("User is already a member of the project");
		}
		/**
		 * Currently only project owner can invite other user for contribution
		 */
		if (!projectMemberRepository.existsById_ProjectIdAndId_UserIdAndProjectRole(projectId, jwtUserPrincipal.id(),
				ProjectRole.OWNER)) {
			throw new PermissionException("User is not the owner of the project");
		}
		Projects projects = getProject(projectId);
		ProjectMember projectMember = ProjectMember.builder().projects(projects)
				.id(new ProjectMemberId(invitedUser.id(), projectId))
				.projectRole(inviteProjectMemberRequestDTO.projectRole()).status(ProjectMemberStatus.PENDING)
				.invitedAt(Instant.now()).build();
		projectMember = projectMemberRepository.save(projectMember);
		return projectMemberMapper.toDto(projectMember, invitedUser);
	}

	@Override
	public ProjectMember getProjectOwner(final Projects projects) {
		return projectMemberRepository.findByProjectsAndProjectRole(projects, ProjectRole.OWNER)
				.orElseThrow(() -> new NotFoundException("Project owner not found"));
	}

	@Override
	public ProjectMemberDTO updateProjectMemberRole(final Long projectId, final Long projectMemberId,
			final String projectRole) {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		Projects projects = getProject(projectId);
		ProjectMember projectOwner = getProjectOwner(projects);
		if (!Objects.equals(projectOwner.getId().getUserId(), jwtUserPrincipal.id())) {
			throw new PermissionException("User is not the owner of the project");
		}
		ProjectMember projectMember = projectMemberRepository.findById(new ProjectMemberId(projectMemberId, projectId))
				.orElseThrow(() -> new NotFoundException("Project member not found with id: " + projectMemberId));
		projectMember.setProjectRole(ProjectRole.valueOf(projectRole));
		projectMember = projectMemberRepository.save(projectMember);
		UserProfileDTO projectMemberUser = null;
		try {
			ResponseEntity<ApiResponse<UserProfileDTO>> response = accountServiceClient
					.getUserById(projectMember.getId().getUserId());

			projectMemberUser = response.getBody().getData();

		} catch (FeignException ex) {
			throw accountServiceExceptionHandler.handle(ex);
		}
		return projectMemberMapper.toDto(projectMember, projectMemberUser);
	}

	@Override
	public void removeProjectMember(final Long projectId, final Long projectMemberId) {
		ProjectMember projectMember = projectMemberRepository.findById(new ProjectMemberId(projectMemberId, projectId))
				.orElseThrow(() -> new NotFoundException("Project member not found with id: " + projectMemberId));

		projectMemberRepository.delete(projectMember);
	}

	@Override
	public void acceptRejectInviteProjectMember(final Long projectId, final String status) {
		if (!ProjectMemberStatus.ACCEPTED.name().equals(status)
				&& !ProjectMemberStatus.REJECTED.name().equals(status)) {
			throw new BadRequestException("Invalid status requested");
		}
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		ProjectMember projectMember = projectMemberRepository
				.findById(new ProjectMemberId(jwtUserPrincipal.id(), projectId))
				.orElseThrow(() -> new NotFoundException("User is not invited"));
		if (!ProjectMemberStatus.PENDING.equals(projectMember.getStatus())) {
			throw new BadRequestException("User has already responded to the invitation");
		}
		projectMember.setStatus(ProjectMemberStatus.valueOf(status));
		projectMemberRepository.save(projectMember);
	}

	private Projects getProject(final Long id) {
		return projectsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Project not found with id: " + id));
	}
}
