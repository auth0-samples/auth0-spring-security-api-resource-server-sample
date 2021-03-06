package com.auth0.example;

import com.auth0.spring.security.api.Auth0SecurityConfig;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AppConfig extends Auth0SecurityConfig {

    /**
     * Not required for the Spring Security implementation, but offers Auth0 API access
     */
    @Bean
    public Auth0Client auth0Client() {
        return new Auth0Client(clientId, issuer);
    }

    /**
     * Override this function in subclass to apply custom authentication / authorization
     * strategies to your appliaction endpoints
     */
    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/profiles").hasAnyAuthority("read:profiles")
                .antMatchers(HttpMethod.GET, "/api/v1/profiles/**").hasAnyAuthority("read:profile", "read:profiles")
                .antMatchers(HttpMethod.POST, "/api/v1/profiles/**").hasAnyAuthority("write:profile")
                .antMatchers(HttpMethod.PUT, "/api/v1/profiles/**").hasAnyAuthority("write:profile")
                .antMatchers(HttpMethod.DELETE, "/api/v1/profiles/**").hasAnyAuthority("delete:profile")
                .antMatchers(securedRoute).authenticated();
    }

}