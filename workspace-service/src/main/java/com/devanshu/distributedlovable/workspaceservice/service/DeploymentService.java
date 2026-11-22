package com.devanshu.distributedlovable.workspaceservice.service;

import com.devanshu.distributedlovable.workspaceservice.dto.deploy.DeployResponseDTO;

public interface DeploymentService {

	DeployResponseDTO deploy(Long id);

}
