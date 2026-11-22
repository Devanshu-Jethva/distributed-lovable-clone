package com.devanshu.distributedlovable.commonlib.service;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.devanshu.distributedlovable.commonlib.service.impl.HelperCommonServiceImpl;

@AutoConfiguration
public class SharedServiceAutoConfiguration {

	@Bean
	HelperCommonService helperCommonService() {
		return new HelperCommonServiceImpl();
	}
}
