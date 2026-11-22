package com.devanshu.distributedlovable.accountservice.service.impl;

import java.time.Instant;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devanshu.distributedlovable.accountservice.dto.subscription.SubscriptionResponseDTO;
import com.devanshu.distributedlovable.accountservice.entity.Plan;
import com.devanshu.distributedlovable.accountservice.entity.Subscription;
import com.devanshu.distributedlovable.accountservice.entity.Users;
import com.devanshu.distributedlovable.accountservice.mapper.SubscriptionMapper;
import com.devanshu.distributedlovable.accountservice.repository.SubscriptionRepository;
import com.devanshu.distributedlovable.accountservice.service.PlanService;
import com.devanshu.distributedlovable.accountservice.service.SubscriptionService;
import com.devanshu.distributedlovable.accountservice.service.UsersService;
import com.devanshu.distributedlovable.commonlib.constants.SubscriptionStatus;
import com.devanshu.distributedlovable.commonlib.dto.PlanResponseDTO;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;
import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;
import com.devanshu.distributedlovable.commonlib.service.HelperCommonService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionServiceImpl implements SubscriptionService {

	SubscriptionRepository subscriptionRepository;
	SubscriptionMapper subscriptionMapper;
	HelperCommonService helperCommonService;
	PlanService planService;
	UsersService usersService;

	@Override
	public SubscriptionResponseDTO getCurrentSubscription() {
		JwtUserPrincipal jwtUserPrincipal = helperCommonService.checkForUserLogin();

		Subscription currentSubscription = subscriptionRepository
				.findByUsersIdAndStatusIn(jwtUserPrincipal.id(), SubscriptionStatus.getActiveSubscriptionStatusList())
				.orElseThrow(() -> new NotFoundException("No active subscription found."));

		return subscriptionMapper.toDto(currentSubscription);
	}

	@Override
	public PlanResponseDTO getCurrentPlan() {
		SubscriptionResponseDTO currentSubscription = getCurrentSubscription();
		return currentSubscription.planResponseDTO();
	}

	@Override
	@Transactional
	public void activateSubscription(final Long userId, final Long planId, final String subscriptionId,
			final String stripeCustomerId) {
		if (subscriptionRepository.existsByStripeSubscriptionId(subscriptionId)) {
			log.error("Subscription already exists");
			return;
		}

		Users users = usersService.getUsers(userId);
		Plan plan = planService.getPlan(planId);

		Subscription subscription = Subscription.builder().users(users).plan(plan).stripeSubscriptionId(subscriptionId)
				.status(SubscriptionStatus.INCOMPLETE).build();

		subscriptionRepository.save(subscription);
	}

	@Override
	@Transactional
	public void updateSubscription(final String stripeSubscriptionId, final SubscriptionStatus status,
			final Instant periodStart, final Instant periodEnd, final Boolean cancelAtPeriodEnd, final Long planId) {
		Subscription subscription = getSubscription(stripeSubscriptionId);

		boolean hasSubscriptionUpdated = false;

		if (status != null && status != subscription.getStatus()) {
			subscription.setStatus(status);
			hasSubscriptionUpdated = true;
		}

		if (periodStart != null && !periodStart.equals(subscription.getStartDate())) {
			subscription.setStartDate(periodStart);
			hasSubscriptionUpdated = true;
		}

		if (periodEnd != null && !periodEnd.equals(subscription.getEndDate())) {
			subscription.setEndDate(periodEnd);
			hasSubscriptionUpdated = true;
		}

		if (cancelAtPeriodEnd != null && !Objects.equals(cancelAtPeriodEnd, subscription.getCancelAtPeriodEnd())) {
			subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
			hasSubscriptionUpdated = true;
		}

		if (planId != null && !planId.equals(subscription.getPlan().getId())) {
			Plan newPlan = planService.getPlan(planId);
			subscription.setPlan(newPlan);
			hasSubscriptionUpdated = true;
		}

		if (hasSubscriptionUpdated) {
			log.debug("Subscription has been updated: {}", stripeSubscriptionId);
			subscriptionRepository.save(subscription);
		}
	}

	@Override
	public void cancelSubscription(final String stripeSubscriptionId) {
		Subscription subscription = getSubscription(stripeSubscriptionId);
		subscription.setStatus(SubscriptionStatus.CANCELED);
		subscriptionRepository.save(subscription);
	}

	@Override
	public void renewSubscriptionPeriod(final String stripeSubscriptionId, final Instant periodStart,
			final Instant periodEnd) {
		Subscription subscription = getSubscription(stripeSubscriptionId);

		Instant newStart = periodStart != null ? periodStart : subscription.getEndDate();
		subscription.setStartDate(newStart);
		subscription.setEndDate(periodEnd);

		if (subscription.getStatus() == SubscriptionStatus.PAST_DUE
				|| subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
			subscription.setStatus(SubscriptionStatus.ACTIVE);
		}

		subscriptionRepository.save(subscription);
	}

	@Override
	public void markSubscriptionPastDue(final String stripeSubscriptionId) {
		Subscription subscription = getSubscription(stripeSubscriptionId);

		if (SubscriptionStatus.PAST_DUE.equals(subscription.getStatus())) {
			log.debug("Subscription is already past due, stripeSubscriptionId: {}", stripeSubscriptionId);
			return;
		}

		subscription.setStatus(SubscriptionStatus.PAST_DUE);
		subscriptionRepository.save(subscription);

		// Notify user via email..
	}

	private Subscription getSubscription(final String stripeSubscriptionId) {
		return subscriptionRepository.findByStripeSubscriptionId(stripeSubscriptionId)
				.orElseThrow(() -> new NotFoundException("Subscription " + stripeSubscriptionId));
	}

}
