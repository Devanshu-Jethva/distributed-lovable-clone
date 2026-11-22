package com.devanshu.distributedlovable.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devanshu.distributedlovable.accountservice.service.SubscriptionService;
import com.devanshu.distributedlovable.accountservice.service.UsersService;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserIdsDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileListDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

	private final UsersService usersService;
	private final SubscriptionService subscriptionService;

	@GetMapping("/user/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable final Long id) {
		UserProfileDTO userProfileDTO = usersService.getUserProfileDTO(id);
		return ResponseEntity.ok(ApiResponse.<UserProfileDTO>builder().data(userProfileDTO)
				.message("User fetched successfully").build());
	}

	@GetMapping("/billing/current-plan")
	public ResponseEntity<Object> getCurrentPlans() {
		PlanResponseDTO planResponseDTO = subscriptionService.getCurrentPlan();
		return ResponseEntity.ok(ApiResponse.<PlanResponseDTO>builder().message("Current plan retrieved successfully")
				.data(planResponseDTO).build());
	}

	@GetMapping("/user/by-email")
	public ResponseEntity<Object> getUserByUsername(@RequestParam final String email) {
		UserProfileDTO userProfileDTO = usersService.getUserByUsername(email);
		return ResponseEntity.ok(ApiResponse.<UserProfileDTO>builder().data(userProfileDTO)
				.message("User fetched successfully").build());
	}

	@GetMapping("/users/bulk")
	ResponseEntity<Object> getUsersByIds(@RequestBody final UserIdsDTO userIdsDTO) {
		return ResponseEntity.ok(
				ApiResponse.<UserProfileListDTO>builder().data(usersService.getUserProfilesByIds(userIdsDTO.userIds()))
						.message("Users fetched successfully").build());
	}

}
