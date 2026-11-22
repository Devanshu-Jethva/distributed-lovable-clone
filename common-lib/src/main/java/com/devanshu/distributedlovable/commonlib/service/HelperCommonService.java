package com.devanshu.distributedlovable.commonlib.service;

import com.devanshu.distributedlovable.commonlib.security.JwtUserPrincipal;

public interface HelperCommonService {

	JwtUserPrincipal checkForUserLogin();
}
