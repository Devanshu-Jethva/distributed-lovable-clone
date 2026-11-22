package com.devanshu.distributedlovable.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devanshu.distributedlovable.accountservice.dto.auth.AuthResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.LoginRequestDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.ProfileResponseDTO;
import com.devanshu.distributedlovable.accountservice.dto.auth.SignupRequestDTO;
import com.devanshu.distributedlovable.accountservice.service.AuthService;
import com.devanshu.distributedlovable.accountservice.service.UsersService;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final UsersService userService;

	@PostMapping("/signup")
	public ResponseEntity<Object> signup(@Valid @RequestBody final SignupRequestDTO signupRequestDTO) {
		AuthResponseDTO authResponseDTO = authService.signup(signupRequestDTO);
		return ResponseEntity.ok(
				ApiResponse.<AuthResponseDTO>builder().message("Sign Up successfully").data(authResponseDTO).build());
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@Valid @RequestBody final LoginRequestDTO loginRequestDTO) {
		AuthResponseDTO authResponseDTO = authService.login(loginRequestDTO);
		return ResponseEntity
				.ok(ApiResponse.<AuthResponseDTO>builder().message("Login successfully").data(authResponseDTO).build());
	}

	@GetMapping("/profile")
	public ResponseEntity<Object> getProfile() {
		ProfileResponseDTO profileResponseDTO = userService.getProfile();
		return ResponseEntity.ok(ApiResponse.<ProfileResponseDTO>builder().message("Profile fetched successfully")
				.data(profileResponseDTO).build());
	}

}
