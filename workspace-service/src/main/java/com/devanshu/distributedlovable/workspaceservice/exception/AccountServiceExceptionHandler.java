package com.devanshu.distributedlovable.workspaceservice.exception;

import org.springframework.stereotype.Component;

import com.devanshu.distributedlovable.commonlib.exceptions.BadRequestException;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;
import com.devanshu.distributedlovable.commonlib.exceptions.PermissionException;

import feign.FeignException;

@Component
public class AccountServiceExceptionHandler {

	public RuntimeException handle(final FeignException exception) {

		return switch (exception.status()) {
		case 400 -> new BadRequestException("Account Service: " + exception.getMessage());
		case 401 -> new PermissionException("Account Service authentication failed");
		case 403 -> new PermissionException("Access denied by Account Service");
		case 404 -> new NotFoundException("Requested resource not found in Account Service");
		case 503 -> new RuntimeException("Account Service is temporarily unavailable");
		default -> new RuntimeException("Account Service communication failed. HTTP status: " + exception.status());
		};
	}
}