package com.devanshu.distributedlovable.accountservice.entity;

import java.io.Serial;
import java.time.Instant;

import com.devanshu.distributedlovable.commonlib.constants.SubscriptionStatus;
import com.devanshu.distributedlovable.commonlib.entity.CommonModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription extends CommonModel {

	@Serial
	private static final long serialVersionUID = 425887524527835986L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", nullable = false)
	Users users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id", nullable = false)
	Plan plan;

	@Column(name = "stripe_subscription_id", nullable = false)
	String stripeSubscriptionId;

	@Column(name = "start_date")
	Instant startDate;

	@Column(name = "end_date")
	Instant endDate;

	@Column(name = "cancelled_date")
	Instant cancelledDate;

	@Builder.Default
	@Column(name = "cancel_at_period_end")
	Boolean cancelAtPeriodEnd = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	SubscriptionStatus status;
}
