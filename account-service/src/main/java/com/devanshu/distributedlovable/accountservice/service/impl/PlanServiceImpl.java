package com.devanshu.distributedlovable.accountservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devanshu.distributedlovable.accountservice.entity.Plan;
import com.devanshu.distributedlovable.accountservice.mapper.PlanMapper;
import com.devanshu.distributedlovable.accountservice.repository.PlanRepository;
import com.devanshu.distributedlovable.accountservice.service.PlanService;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanServiceImpl implements PlanService {

	PlanRepository planRepository;
	PlanMapper planMapper;

	@Override
	public List<PlanResponseDTO> getPlans() {
		return planMapper.toDtos(planRepository.findAll());
	}

	@Override
	public Plan getPlan(final Long planId) {
		return planRepository.findById(planId).orElseThrow(() -> new NotFoundException("Plan " + planId));

	}

}
