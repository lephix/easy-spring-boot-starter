package com.lephix.easy.security;

import com.lephix.easy.autoconfiguration.EasyProperties;
import com.lephix.easy.security.impl.NoRedirectionEntryPoint;
import com.lephix.easy.security.impl.NoRedirectionAuthFailureHandler;
import com.lephix.easy.security.impl.NoRedirectionAuthSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.NotSupportedException;

@ConditionalOnExpression("'${easy.security.source}' matches '(memory|jdbc|fake)'")
@ConditionalOnWebApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@Slf4j
public class EasyWebSecurityConfig extends AbstractWebSecurityConfig {

    @Autowired
    private EasyProperties easyProperties;
    @Autowired(required = false)
    private UserDetailsService userDetailsService;
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.formLogin()
                .successHandler(new NoRedirectionAuthSuccessHandler())
                .failureHandler(new NoRedirectionAuthFailureHandler()).and()
                .exceptionHandling()
                .authenticationEntryPoint(new NoRedirectionEntryPoint()).and()
                .logout();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String securitySource = easyProperties.getSecurity().getSource();
        switch (securitySource) {
            case "memory":
                initMemoryAuth(auth);
                break;
            case "jdbc":
            case "fake":
                initCustomAuth(auth);
                break;
            default:
                throw new NotSupportedException("easy.security.source not supported: " + securitySource);
        }
    }

    private void initMemoryAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN")
                .and()
                .withUser("user").password("user").roles("USER");
        log.info("Initialized AuthenticationManager with 'memory' source.");
    }

    private void initCustomAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder == null ? NoOpPasswordEncoder.getInstance() : passwordEncoder);
        log.info("Initialized AuthenticationManager with '{}' source.", userDetailsService.getClass().getName());
    }

}
