package com.devanshu.distributedlovable.accountservice.service;

import com.devanshu.distributedlovable.accountservice.dto.auth.AuthResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.LoginRequestDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.SignupRequestDTO;

public interface AuthService {
	AuthResponseDTO signup(SignupRequestDTO signupRequestDTO);

	AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
