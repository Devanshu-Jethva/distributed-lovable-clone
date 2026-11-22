package com.devanshu.distributedlovable.accountservice.dto.subscription;

import java.time.Instant;

import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;

public record SubscriptionResponseDTO(PlanResponseDTO planResponseDTO, String status, Instant endDate, Long tokenUsed) {
}
