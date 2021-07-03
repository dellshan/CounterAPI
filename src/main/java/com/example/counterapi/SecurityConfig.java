package com.example.counterapi;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static String REALM = "SPRING SECURITY DEMO";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BasicAuthenticationEntryPoint getBasicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName(REALM);
        return entryPoint;
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("optus")
                .password(new BCryptPasswordEncoder().encode("candidates")).roles("USER");

    }
}
