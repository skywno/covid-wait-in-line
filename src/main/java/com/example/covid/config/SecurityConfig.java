package com.example.covid.config;

import com.example.covid.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(
            HttpSecurity http,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired AdminService adminService
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(adminService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/events/**", "/locations/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                .formLogin() // enable form based log in
                    .permitAll()
                    .loginPage("/login")
                    .defaultSuccessUrl("/admin/locations")
                .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/locations")
                    .logoutUrl("/logout");

        return http.build();
    }

}
