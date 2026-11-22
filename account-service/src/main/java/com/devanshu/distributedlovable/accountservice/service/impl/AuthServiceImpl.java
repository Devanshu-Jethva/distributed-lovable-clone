package com.devanshu.distributedlovable.accountservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devanshu.distributedlovable.accountservice.dto.auth.AuthResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.LoginRequestDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.SignupRequestDTO;
import com.devanshu.distributedlovable.accountservice.entity.Users;
import com.devanshu.distributedlovable.accountservice.mapper.UsersMapper;
import com.devanshu.distributedlovable.accountservice.repository.UsersRepository;
import com.devanshu.distributedlovable.accountservice.service.AuthService;
import com.devanshu.distributedlovable.commonlib.exceptions.BadRequestException;
import com.devanshu.distributedlovable.commonlib.security.JwtUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

	UsersRepository usersRepository;
	UsersMapper usersMapper;
	PasswordEncoder passwordEncoder;
	JwtUtil jwtUtil;

	@Override
	public AuthResponseDTO signup(final SignupRequestDTO signupRequestDTO) {
		usersRepository.findByUsernameIgnoreCase(signupRequestDTO.username()).ifPresent(users -> {
			throw new BadRequestException("Username is already in use");
		});

		Users users = usersMapper.toEntity(signupRequestDTO);
		users.setPassword(passwordEncoder.encode(users.getPassword()));
		users = usersRepository.save(users);

		return new AuthResponseDTO(jwtUtil.generateToken(usersMapper.toUserDto(users)), usersMapper.toDto(users));
	}

	@Override
	public AuthResponseDTO login(final LoginRequestDTO loginRequestDTO) {
		Users users = usersRepository.findByUsernameIgnoreCase(loginRequestDTO.username())
				.orElseThrow(() -> new BadRequestException("Invalid username or password"));

		if (users.getDeletedAt() != null) {
			throw new BadRequestException("Account deleted");
		}

		if (!passwordEncoder.matches(loginRequestDTO.password(), users.getPassword())) {
			throw new BadRequestException("Invalid username or password");
		}

		return new AuthResponseDTO(jwtUtil.generateToken(usersMapper.toUserDto(users)), usersMapper.toDto(users));
	}
}
