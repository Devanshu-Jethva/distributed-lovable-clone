package com.devanshu.distributedlovable.workspaceservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectDetailDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectListDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;

@Mapper(componentModel = "spring")
public interface ProjectsMapper {

	ProjectDetailDTO toDetailDto(Projects save);

	List<ProjectListDTO> toProjectListDTOs(List<Projects> projects);
}
