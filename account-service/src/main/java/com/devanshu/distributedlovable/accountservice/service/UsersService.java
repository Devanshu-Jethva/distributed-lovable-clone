package com.devanshu.distributedlovable.accountservice.service;

import java.util.List;

import com.devanshu.distributedlovable.accountservice.dto.auth.ProfileResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.UsersListDTO;
import com.devanshu.distributedlovable.accountservice.entity.Users;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileListDTO;

public interface UsersService {
	ProfileResponseDTO getProfile();

	Users getUsers(Long id);

	UsersListDTO getAllUsers();

	UserProfileDTO getUserProfileDTO(Long id);

	UserProfileDTO getUserByUsername(String email);

	UserProfileListDTO getUserProfilesByIds(List<Long> userIds);
}
