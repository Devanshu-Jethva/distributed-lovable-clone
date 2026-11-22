package com.devanshu.distributedlovable.accountservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devanshu.distributedlovable.accountservice.service.PlanService;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

	private final PlanService planService;

	@GetMapping
	public ResponseEntity<Object> getPlans() {
		List<PlanResponseDTO> planResponseListDTOS = planService.getPlans();
		return ResponseEntity.ok(ApiResponse.<List<PlanResponseDTO>>builder().message("Plans retrieved successfully")
				.data(planResponseListDTOS).build());
	}

}
