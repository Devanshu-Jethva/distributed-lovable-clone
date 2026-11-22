package com.devanshu.distributedlovable.workspaceservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devanshu.distributedlovable.commonlib.constants.ApplicationConstants;
import com.devanshu.distributedlovable.commonlib.constants.ProjectMemberStatus;
import com.devanshu.distributedlovable.commonlib.constants.ProjectRole;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;
import com.devanshu.distributedlovable.commonlib.exceptions.BadRequestException;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;
import com.devanshu.distributedlovable.commonlib.exceptions.PermissionException;
import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;
import com.devanshu.distributedlovable.commonlib.service.HelperCommonService;
import com.devanshu.distributedlovable.workspaceservice.client.AccountServiceClient;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectDetailDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectListDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectRequestDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMember;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMemberId;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;
import com.devanshu.distributedlovable.workspaceservice.exception.AccountServiceExceptionHandler;
import com.devanshu.distributedlovable.workspaceservice.mapper.ProjectsMapper;
import com.devanshu.distributedlovable.workspaceservice.repository.ProjectMemberRepository;
import com.devanshu.distributedlovable.workspaceservice.repository.ProjectsRepository;
import com.devanshu.distributedlovable.workspaceservice.service.ProjectMemberService;
import com.devanshu.distributedlovable.workspaceservice.service.ProjectTemplateService;
import com.devanshu.distributedlovable.workspaceservice.service.ProjectsService;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectsServiceImpl implements ProjectsService {

	ProjectsRepository projectsRepository;
	ProjectsMapper projectsMapper;
	ProjectMemberRepository projectMemberRepository;
	ProjectMemberService projectMemberService;
	HelperCommonService helperCommonService;
	ProjectTemplateService projectTemplateService;
	AccountServiceClient accountServiceClient;
	AccountServiceExceptionHandler accountServiceExceptionHandler;

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public ProjectDetailDTO createProject(final ProjectRequestDTO projectRequestDTO) {
		if (!canCreateNewProject()) {
			throw new BadRequestException("Subscription limit reached, Can't create new project anymore.");
		}

		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();

		Projects projects = Projects.builder().name(projectRequestDTO.name())
				.description(projectRequestDTO.description()).build();
		projects = projectsRepository.save(projects);

		ProjectMember projectMember = ProjectMember.builder()
				.id(new ProjectMemberId(jwtUserPrincipal.id(), projects.getId())).projects(projects)
				.projectRole(ProjectRole.OWNER).invitedAt(Instant.now()).status(ProjectMemberStatus.ACCEPTED).build();
		projectMemberRepository.save(projectMember);

		projectTemplateService.initializeProjectFromTemplate(projects.getId());

		return projectsMapper.toDetailDto(projects);
	}

	private boolean canCreateNewProject() {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		PlanResponseDTO currentPlanResponseDTO = null;

		try {
			ResponseEntity<ApiResponse<PlanResponseDTO>> currentPlans = accountServiceClient.getCurrentPlans();
			currentPlanResponseDTO = currentPlans.getBody().getData();
		} catch (FeignException ex) {
			throw accountServiceExceptionHandler.handle(ex);
		}

		int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(jwtUserPrincipal.id());

		if (currentPlanResponseDTO == null) {
			return countOfOwnedProjects < ApplicationConstants.FREE_TIER_PROJECTS_ALLOWED;
		}
		return countOfOwnedProjects < currentPlanResponseDTO.maxProjects();
	}

	@Override
	public List<ProjectListDTO> getAllProjects() {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		List<Projects> projects = projectsRepository.getAllAccessibleProjects(jwtUserPrincipal.id());
		return projectsMapper.toProjectListDTOs(projects);
	}

	@Override
	public ProjectDetailDTO getProjectById(final Long id) {
		return projectsMapper.toDetailDto(getProject(id));
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public ProjectDetailDTO updateProject(final ProjectRequestDTO projectRequestDTO) {
		if (projectRequestDTO.id() == null) {
			throw new BadRequestException("Project id is required for update");
		}
		Projects projects = getProject(projectRequestDTO.id());
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		ProjectMember projectOwner = projectMemberService.getProjectOwner(projects);
		if (!Objects.equals(projectOwner.getId().getUserId(), jwtUserPrincipal.id())) {
			throw new PermissionException("User is not the owner of the project");
		}
		if (projects.getDeletedAt() != null) {
			throw new BadRequestException("Project is deleted and cannot be updated");
		}
		projects.setName(projectRequestDTO.name());
		projects.setDescription(projectRequestDTO.description());
		projects = projectsRepository.save(projects);
		return projectsMapper.toDetailDto(projects);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteProject(final Long id) {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		Projects projects = getProject(id);
		ProjectMember projectOwner = projectMemberService.getProjectOwner(projects);
		if (!Objects.equals(projectOwner.getId().getUserId(), jwtUserPrincipal.id())) {
			throw new PermissionException("User is not the owner of the project");
		}
		if (projects.getDeletedAt() != null) {
			throw new BadRequestException("Project is already deleted");
		}
		projects.setDeletedAt(Instant.now());
		projectsRepository.save(projects);
	}

	@Override
	public Projects getProject(final Long id) {
		return projectsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Project with " + id + " not found"));
	}

}
