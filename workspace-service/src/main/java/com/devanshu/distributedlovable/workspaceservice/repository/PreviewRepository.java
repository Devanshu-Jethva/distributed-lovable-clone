package com.devanshu.distributedlovable.workspaceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devanshu.distributedlovable.workspaceservice.entity.Preview;

public interface PreviewRepository extends JpaRepository<Preview, Long> {
}
