package com.devanshu.distributedlovable.workspaceservice.dto.projects;

import java.time.Instant;

public record ProjectDetailDTO(Long id, String name, String description, Instant createdAt,
                               Instant updatedAt) {
}
