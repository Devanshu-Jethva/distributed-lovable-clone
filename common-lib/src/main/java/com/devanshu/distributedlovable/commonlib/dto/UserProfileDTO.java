package com.devanshu.distributedlovable.commonlib.dto;

import java.time.Instant;

public record UserProfileDTO(Long id, String name, String username, Instant deletedAt) {
}
