package com.devanshu.distributedlovable.accountservice.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devanshu.distributedlovable.accountservice.entity.Subscription;
import com.devanshu.distributedlovable.commonlib.constants.SubscriptionStatus;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Optional<Subscription> findByUsersIdAndStatusIn(Long id, Set<SubscriptionStatus> active);

	boolean existsByStripeSubscriptionId(String subscriptionId);

	Optional<Subscription> findByStripeSubscriptionId(String gatewaySubscriptionId);
}
