package com.devanshu.distributedlovable.commonlib.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserIdsDTO(
		@NotEmpty(message = "User ids required") List<@NotNull(message = "User is cannot be null") Long> userIds) {

}
