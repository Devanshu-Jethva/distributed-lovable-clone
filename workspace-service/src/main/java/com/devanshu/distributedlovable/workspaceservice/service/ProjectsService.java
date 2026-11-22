package com.devanshu.distributedlovable.workspaceservice.service;

import java.util.List;

import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectDetailDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectListDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.ProjectRequestDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;

public interface ProjectsService {

	List<ProjectListDTO> getAllProjects();

	ProjectDetailDTO getProjectById(Long id);

	ProjectDetailDTO createProject(ProjectRequestDTO projectRequestDTO);

	ProjectDetailDTO updateProject(ProjectRequestDTO projectRequestDTO);

	void deleteProject(Long id);

	Projects getProject(Long id);
}
