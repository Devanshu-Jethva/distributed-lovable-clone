package com.devanshu.distributedlovable.workspaceservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devanshu.distributedlovable.commonlib.constants.ProjectRole;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMember;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMemberId;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

	boolean existsById_ProjectIdAndId_UserId(Long projectId, Long userId);

	List<ProjectMember> findById_ProjectId(Long projectId);

	Optional<ProjectMember> findByProjectsAndProjectRole(Projects projects, ProjectRole projectRole);

	@Query("""
			Select pm.projectRole from ProjectMember pm
			where pm.id.userId = :userId
			and pm.id.projectId = :projectId
			""")
	Optional<ProjectRole> findRoleByProjectIdAndUserId(Long projectId, Long userId);

	@Query("""
			SELECT COUNT(pm) FROM ProjectMember pm
			WHERE pm.id.userId = :userId AND pm.projectRole = 'OWNER'
			""")
	int countProjectOwnedByUser(@Param("userId") Long userId);

	boolean existsById_ProjectIdAndId_UserIdAndProjectRole(Long projectId, Long userId, ProjectRole projectRole);
}
