package com.devanshu.distributedlovable.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class AccountServiceApplication {

	public static void main(final String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
