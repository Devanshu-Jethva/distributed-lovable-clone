package com.devanshu.distributedlovable.accountservice.service;

import java.util.Map;

import com.devanshu.distributedlovable.accountservice.dto.subscription.CheckoutRequestDTO;
import com.stripe.model.StripeObject;

public interface PaymentProcessor {

	String createPaymentCheckoutUrl(CheckoutRequestDTO checkoutRequestDTO);

	String getPaymentPortalUrl();

	void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
