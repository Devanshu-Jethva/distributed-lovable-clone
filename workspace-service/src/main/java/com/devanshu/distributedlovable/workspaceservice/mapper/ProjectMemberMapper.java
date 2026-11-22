package com.devanshu.distributedlovable.workspaceservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.workspaceservice.dto.projects.member.ProjectMemberDTO;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectMember;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

	@Mapping(target = "userId", source = "userProfileDTO.id")
	@Mapping(target = "name", source = "userProfileDTO.name")
	@Mapping(target = "username", source = "userProfileDTO.username")
	@Mapping(target = "projectId", source = "projectMember.id.projectId")
	ProjectMemberDTO toDto(ProjectMember projectMember, UserProfileDTO userProfileDTO);

}
