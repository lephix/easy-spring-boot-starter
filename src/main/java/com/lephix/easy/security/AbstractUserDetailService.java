package com.lephix.easy.security;

import com.google.common.base.Splitter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractUserDetailService implements UserDetailsService {

    public static List<GrantedAuthority> buildAuthorities(String authorities) {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(authorities)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
