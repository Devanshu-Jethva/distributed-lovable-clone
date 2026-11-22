package com.devanshu.distributedlovable.accountservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devanshu.distributedlovable.accountservice.dto.auth.ProfileResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.UsersListDTO;
import com.devanshu.distributedlovable.accountservice.entity.Users;
import com.devanshu.distributedlovable.accountservice.mapper.UsersMapper;
import com.devanshu.distributedlovable.accountservice.repository.UsersRepository;
import com.devanshu.distributedlovable.accountservice.service.UsersService;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileListDTO;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;
import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;
import com.devanshu.distributedlovable.commonlib.service.HelperCommonService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersServiceImpl implements UsersService {

	UsersRepository usersRepository;
	HelperCommonService helperCommonService;
	UsersMapper usersMapper;

	@Override
	public ProfileResponseDTO getProfile() {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();
		Long userId = jwtUserPrincipal.id();
		return usersMapper.toProfileResponseDTO(getUsers(userId));
	}

	@Override
	public Users getUsers(final Long id) {
		return usersRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id " + id));
	}

	@Override
	public UserProfileDTO getUserProfileDTO(final Long id) {
		return usersMapper.toDto(getUsers(id));
	}

	@Override
	public UsersListDTO getAllUsers() {
		return new UsersListDTO(usersRepository.findAll().stream().map(user -> usersMapper.toDto(user)).toList());
	}

	@Override
	public UserProfileDTO getUserByUsername(final String email) {
		return usersMapper.toDto(usersRepository.findByUsernameIgnoreCase(email)
				.orElseThrow(() -> new NotFoundException("User not found")));
	}

	@Override
	@Transactional(readOnly = true)
	public UserProfileListDTO getUserProfilesByIds(final List<Long> userIds) {

		List<Long> distinctUserIds = userIds.stream().distinct().toList();

		List<Users> users = usersRepository.findAllById(distinctUserIds);

		return new UserProfileListDTO(usersMapper.toDtos(users));
	}
}
