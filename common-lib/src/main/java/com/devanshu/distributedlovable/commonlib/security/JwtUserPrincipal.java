package com.devanshu.distributedlovable.commonlib.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public record JwtUserPrincipal(Long id, String username, List<? extends GrantedAuthority> authorities) {
}
