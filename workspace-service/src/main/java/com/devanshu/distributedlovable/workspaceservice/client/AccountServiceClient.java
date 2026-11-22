package com.devanshu.distributedlovable.workspaceservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserIdsDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;
import com.devanshu.distributedlovable.commonlib.dto.UserProfileListDTO;

@FeignClient(name = "account-service", path = "/account")
public interface AccountServiceClient {

	@GetMapping("/inernal/user/{id}")
	ResponseEntity<ApiResponse<UserProfileDTO>> getUserById(@PathVariable final Long id);

	@GetMapping("/inernal/user/by-email")
	ResponseEntity<ApiResponse<UserProfileDTO>> getUserByEmail(@RequestParam final String email);

	@GetMapping("/inernal/billing/current-plan")
	ResponseEntity<ApiResponse<PlanResponseDTO>> getCurrentPlans();

	@GetMapping("/inernal/users/bulk")
	ResponseEntity<ApiResponse<UserProfileListDTO>> getUsersByIds(@RequestBody UserIdsDTO userIdsDTO);

}
