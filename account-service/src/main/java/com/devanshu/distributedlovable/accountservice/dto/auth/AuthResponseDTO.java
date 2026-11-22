package com.devanshu.distributedlovable.accountservice.dto.auth;

import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;

public record AuthResponseDTO(String token, UserProfileDTO userProfileDTO) {
}
