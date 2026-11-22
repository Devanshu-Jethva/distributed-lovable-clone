package com.devanshu.distributedlovable.workspaceservice.dto.projects.member;

import java.time.Instant;

import com.devanshu.distributedlovable.commonlib.constants.ProjectRole;

public record ProjectMemberDTO(Long userId, String name, String username, ProjectRole projectRole, Instant invitedAt,
		Long projectId) {
}
