package com.devanshu.distributedlovable.accountservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devanshu.distributedlovable.accountservice.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUsernameIgnoreCase(String username);
}
