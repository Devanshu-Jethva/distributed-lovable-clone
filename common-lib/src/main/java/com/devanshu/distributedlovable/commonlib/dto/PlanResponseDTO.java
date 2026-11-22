package com.devanshu.distributedlovable.commonlib.dto;

public record PlanResponseDTO(Long id, String name, Integer maxProjects, Long maxTokensPerDay, Integer maxPreviews,
		Boolean unlimitedAi, Long price, Boolean active) {
}
