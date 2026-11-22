package com.devanshu.distributedlovable.accountservice.service;

import java.util.List;

import com.devanshu.distributedlovable.accountservice.entity.Plan;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;

public interface PlanService {

	List<PlanResponseDTO> getPlans();

	Plan getPlan(Long planId);

}
