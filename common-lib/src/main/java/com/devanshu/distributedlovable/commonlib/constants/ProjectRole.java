package com.devanshu.distributedlovable.commonlib.constants;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectRole {
	EDITOR(ProjectPermission.VIEW, ProjectPermission.EDIT, ProjectPermission.DELETE, ProjectPermission.VIEW_MEMBERS),
	VIEWER(ProjectPermission.VIEW, ProjectPermission.VIEW_MEMBERS),
	OWNER(ProjectPermission.VIEW, ProjectPermission.EDIT, ProjectPermission.DELETE, ProjectPermission.MANAGE_MEMBERS,
			ProjectPermission.VIEW_MEMBERS);

	private final Set<ProjectPermission> permissions;

	ProjectRole(final ProjectPermission... permissions) {
		this.permissions = Set.of(permissions);
	}
}
