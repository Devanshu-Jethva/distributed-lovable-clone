package com.devanshu.distributedlovable.workspaceservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devanshu.distributedlovable.workspaceservice.entity.ProjectFile;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
	Optional<ProjectFile> findByProjects_IdAndPath(Long projectId, String cleanPath);

	List<ProjectFile> findByProjects_Id(Long id);

}
