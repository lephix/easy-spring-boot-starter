package com.lephix.easy.security.impl;

import com.lephix.easy.security.AbstractUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * return
 */
@ConditionalOnProperty(prefix = "easy", name = "security.source", havingValue = "fake")
@Component
public class FakeUserDetailService extends AbstractUserDetailService {

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.hasText(username)) {
            return fakeUser(passwordEncoder, username);
        }
        throw new UsernameNotFoundException("Username is empty.");
    }

    private static UserDetails fakeUser(PasswordEncoder passwordEncoder, String username) {
        String password = passwordEncoder == null ? username : passwordEncoder.encode(username);
        return new User(username, password, buildAuthorities("ROLE_ADMIN"));
    }
}
