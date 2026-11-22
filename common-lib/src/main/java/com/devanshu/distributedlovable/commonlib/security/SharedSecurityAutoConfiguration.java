package com.devanshu.distributedlovable.commonlib.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devanshu.distributedlovable.commonlib.config.UsernameAuditorAware;
import com.devanshu.distributedlovable.commonlib.config.properties.JwtPropertiesConfig;

import feign.RequestInterceptor;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableJpaAuditing
@EnableConfigurationProperties(JwtPropertiesConfig.class)
public class SharedSecurityAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	JwtUtil jwtUtil(final JwtPropertiesConfig jwtPropertiesConfig) {
		return new JwtUtil(jwtPropertiesConfig);
	}

	@Bean
	@ConditionalOnMissingBean
	JwtAuthenticationFilter jwtAuthenticationFilter(final JwtUtil jwtUtil, final ObjectMapper objectMapper) {
		return new JwtAuthenticationFilter(jwtUtil, objectMapper);
	}

	@Bean
	@ConditionalOnMissingBean(AuditorAware.class)
	AuditorAware<Long> auditorAware() {
		return new UsernameAuditorAware();
	}

	/**
	 * Global interceptor for each feign client calls <br/>
	 * It adds jwt token in header
	 */
	@Bean
	RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.getCredentials() instanceof String token) {
				requestTemplate.header("Authorization", "Bearer " + token);
			}
		};
	}
}
