package com.devanshu.distributedlovable.commonlib.service.impl;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;
import com.devanshu.distributedlovable.commonlib.service.HelperCommonService;

@Service
public class HelperCommonServiceImpl implements HelperCommonService {

	@Override
	public JwtUserPrincipal checkForUserLogin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| !(authentication.getPrincipal() instanceof JwtUserPrincipal)) {
			throw new AuthenticationCredentialsNotFoundException("Please login first");
		}
		return (JwtUserPrincipal) authentication.getPrincipal();
	}
}
