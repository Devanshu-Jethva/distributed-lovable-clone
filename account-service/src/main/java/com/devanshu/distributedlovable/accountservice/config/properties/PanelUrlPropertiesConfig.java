package com.devanshu.distributedlovable.accountservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "panel-url")
public class PanelUrlPropertiesConfig {

	@NotBlank
	private String admin;
}