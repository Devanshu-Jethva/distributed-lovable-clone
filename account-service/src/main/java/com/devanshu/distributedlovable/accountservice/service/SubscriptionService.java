package com.devanshu.distributedlovable.accountservice.service;

import java.time.Instant;

import com.devanshu.distributedlovable.accountservice.dto.subscription.SubscriptionResponseDTO;
import com.devanshu.distributedlovable.commonlib.constants.SubscriptionStatus;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;

public interface SubscriptionService {
	SubscriptionResponseDTO getCurrentSubscription();

	void activateSubscription(Long userId, Long planId, String subscriptionId, String stripeCustomerId);

	void updateSubscription(String stripeSubscriptionId, SubscriptionStatus status, Instant periodStart,
			Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

	void cancelSubscription(String stripeSubscriptionId);

	void renewSubscriptionPeriod(String stripeSubscriptionId, Instant periodStart, Instant periodEnd);

	void markSubscriptionPastDue(String stripeSubscriptionId);

	PlanResponseDTO getCurrentPlan();
}
