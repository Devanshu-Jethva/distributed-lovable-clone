package com.devanshu.distributedlovable.accountservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.devanshu.distributedlovable.accountservice.entity.Plan;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;

@Mapper(componentModel = "spring")
public interface PlanMapper {

	PlanResponseDTO toDto(Plan plan);

	List<PlanResponseDTO> toDtos(List<Plan> plans);
}
