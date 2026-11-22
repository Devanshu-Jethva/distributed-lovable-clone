package com.devanshu.distributedlovable.commonlib.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;

public class UsernameAuditorAware implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null
				|| "anonymousUser".equals(authentication.getPrincipal())
				|| !(authentication.getPrincipal() instanceof JwtUserPrincipal)) {
			return Optional.ofNullable(1L);
		}

		return Optional.ofNullable(((JwtUserPrincipal) authentication.getPrincipal()).id());
	}
}
