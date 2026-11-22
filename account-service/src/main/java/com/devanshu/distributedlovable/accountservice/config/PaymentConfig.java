package com.devanshu.distributedlovable.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devanshu.distributedlovable.accountservice.config.properties.StripePropertiesConfig;
import com.stripe.StripeClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PaymentConfig {

	private final StripePropertiesConfig stripePropertiesConfig;

	@Bean
	StripeClient stripeClient() {
		return StripeClient.builder().setApiKey(stripePropertiesConfig.getApi().getSecret()).build();
	}

}
