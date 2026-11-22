package com.devanshu.distributedlovable.workspaceservice.dto.projects.member;

import com.devanshu.distributedlovable.commonlib.constants.ProjectRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteProjectMemberRequestDTO(@NotBlank(message = "Username is required") String username,
		@NotNull(message = "Project role is required") ProjectRole projectRole) {
}
