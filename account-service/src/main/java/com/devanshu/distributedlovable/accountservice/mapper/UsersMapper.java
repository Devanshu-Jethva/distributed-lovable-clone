package com.devanshu.distributedlovable.accountservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devanshu.distributedlovable.accountservice.dto.auth.ProfileResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.SignupRequestDTO;
import com.devanshu.distributedlovable.accountservice.entity.Users;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.commonlib.dto.UsersDTO;

@Mapper(componentModel = "spring")
public interface UsersMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "stripeCustomerId", ignore = true)
	Users toEntity(SignupRequestDTO signupRequestDTO);

	UserProfileDTO toDto(Users users);

	List<UserProfileDTO> toDtos(List<Users> users);

	ProfileResponseDTO toProfileResponseDTO(Users users);

	UsersDTO toUserDto(Users users);
}
