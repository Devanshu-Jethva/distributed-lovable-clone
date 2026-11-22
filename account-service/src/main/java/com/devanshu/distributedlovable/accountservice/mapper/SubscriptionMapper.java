package com.devanshu.distributedlovable.accountservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devanshu.distributedlovable.accountservice.dto.subscription.SubscriptionResponseDTO;
import com.devanshu.distributedlovable.accountservice.entity.Subscription;

@Mapper(componentModel = "spring", uses = PlanMapper.class)
public interface SubscriptionMapper {

	@Mapping(source = "plan", target = "planResponseDTO")
	@Mapping(target = "tokenUsed", ignore = true)
	SubscriptionResponseDTO toDto(Subscription currentSubscription);

}
