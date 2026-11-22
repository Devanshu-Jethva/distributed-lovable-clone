package com.devanshu.distributedlovable.commonlib.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtPropertiesConfig {

	@NotBlank(message = "JWT secret must not be blank")
	private String secret;
}
