package com.devanshu.distributedlovable.workspaceservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.devanshu.distributedlovable.workspaceservice.dto.projects.file.ProjectFileListDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectFile;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

//    ProjectListDTO toDto(ProjectFile projectFile);

	List<ProjectFileListDTO> toProjectListDTO(List<ProjectFile> projectFileList);
}
