package com.devanshu.distributedlovable.workspaceservice.dto.projects.file;

public record ProjectFileListDTO(String path) {

	@Override
	public String toString() {
		return path;
	}

}
