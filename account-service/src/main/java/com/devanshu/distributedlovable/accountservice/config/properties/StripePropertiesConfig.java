package com.devanshu.distributedlovable.accountservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "stripe")
public class StripePropertiesConfig {

	private Api api;

	private Webhook webhook;

	@Data
	public static class Api {
		@NotBlank
		private String secret;
	}

	@Data
	public static class Webhook {
		@NotBlank
		private String secret;
	}
}
