package com.ray.dormitory.config.security;

import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public JWTRequestFilter jwtRequestFilter(UserDetailsService userDetailsService) {
        return new JWTRequestFilter(userDetailsService);
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JWTAuthenticationEntryPoint();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationConfiguration authenticationConfiguration, JWTRequestFilter jwtRequestFilter) throws Exception {
        return httpSecurity.csrf().disable()
                .cors()
                .and()
                .sessionManagement().disable()

                .authorizeHttpRequests()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/articleTypes"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/articles/indexShow"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/login"))
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationManager(authenticationConfiguration.getAuthenticationManager())
                .headers().frameOptions().disable()


                .and()
                .formLogin().disable()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new JWTAuthenticationEntryPoint())
                .and()
                .build();

    }

    @Bean
    public UserDetailsService jwtUserService(UserService userService) {
        return new JWTUserService(userService);
    }


}
