package com.devanshu.distributedlovable.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devanshu.distributedlovable.accountservice.dto.auth.UsersListDTO;
import com.devanshu.distributedlovable.accountservice.service.UsersService;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

	private final UsersService userService;

	@GetMapping
	public ResponseEntity<Object> getAllUsers() {
		UsersListDTO usersListDTO = userService.getAllUsers();
		return ResponseEntity.ok(
				ApiResponse.<UsersListDTO>builder().data(usersListDTO).message("Users fetched successfully").build());
	}

}
