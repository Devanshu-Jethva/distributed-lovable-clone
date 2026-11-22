package com.devanshu.distributedlovable.workspaceservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devanshu.distributedlovable.workspaceservice.entity.Projects;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {

	@Query("""
			select p
			   from Projects p
			   join ProjectMember pm on pm.id.projectId = p.id
			   where pm.id.userId = :ownerId
			     and p.deletedAt IS NULL
			   order by p.updatedAt desc
			""")
	List<Projects> getAllAccessibleProjects(Long ownerId);

}
