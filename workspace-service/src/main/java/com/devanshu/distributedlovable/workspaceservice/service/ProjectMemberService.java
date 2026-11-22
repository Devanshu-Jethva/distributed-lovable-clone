package com.devanshu.distributedlovable.workspaceservice.service;

import java.util.List;

import com.devanshu.distributedlovable.workspaceservice.dto.projects.member.InviteProjectMemberRequestDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.member.ProjectMemberDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMember;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;

public interface ProjectMemberService {

	List<ProjectMemberDTO> getProjectMembers(Long projectId);

	ProjectMemberDTO inviteProjectMember(Long projectId, InviteProjectMemberRequestDTO inviteProjectMemberRequestDTO);

	ProjectMember getProjectOwner(Projects projects);

	ProjectMemberDTO updateProjectMemberRole(Long projectId, Long projectMemberId, String projectRole);

	void removeProjectMember(Long projectId, Long projectMemberId);

	void acceptRejectInviteProjectMember(Long projectId, String status);
}
