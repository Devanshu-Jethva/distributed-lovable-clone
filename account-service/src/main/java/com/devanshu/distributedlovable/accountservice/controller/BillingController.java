package com.devanshu.distributedlovable.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devanshu.distributedlovable.accountservice.dto.subscription.CheckoutRequestDTO;
import com.devanshu.distributedlovable.accountservice.dto.subscription.SubscriptionResponseDTO;
import com.devanshu.distributedlovable.accountservice.service.PaymentProcessor;
import com.devanshu.distributedlovable.accountservice.service.SubscriptionService;
import com.devanshu.distributedlovable.commonlib.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillingController {

	private final SubscriptionService subscriptionService;
	private final PaymentProcessor paymentProcessor;

	@GetMapping("/subscription")
	public ResponseEntity<Object> getSubscription() {
		SubscriptionResponseDTO subscriptionResponseDTO = subscriptionService.getCurrentSubscription();
		return ResponseEntity.ok(ApiResponse.<SubscriptionResponseDTO>builder()
				.message("Subscription retrieved successfully").data(subscriptionResponseDTO).build());
	}

	@PostMapping("/payments/checkout")
	public ResponseEntity<Object> getStripeCheckoutUrl(@RequestBody final CheckoutRequestDTO checkoutRequestDTO) {
		String checkoutUrl = paymentProcessor.createPaymentCheckoutUrl(checkoutRequestDTO);
		return ResponseEntity.status(HttpStatus.SEE_OTHER).body(
				ApiResponse.<String>builder().message("Checkout URL retrieved successfully").data(checkoutUrl).build());
	}

	@GetMapping("/payments/portal")
	public ResponseEntity<Object> getStripePortalUrl() {
		String portalUrl = paymentProcessor.getPaymentPortalUrl();
		return ResponseEntity
				.ok(ApiResponse.<String>builder().message("Portal URL retrieved successfully").data(portalUrl).build());
	}
}
