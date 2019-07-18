package com.lephix.easy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@ConditionalOnProperty(prefix = "easy", name = "security.source", havingValue = "oauth2")
@ConditionalOnWebApplication
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class EasyOAuth2WebSecurityConfig extends AbstractWebSecurityConfig {

    @Autowired(required = false)
    private AuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        OAuth2LoginConfigurer config = http.oauth2Login();
        if (successHandler != null) {
            config.successHandler(successHandler);
        }
        http.logout();
    }

}
