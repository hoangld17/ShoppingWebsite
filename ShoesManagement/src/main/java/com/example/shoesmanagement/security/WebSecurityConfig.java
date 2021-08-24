package com.example.shoesmanagement.security;


import com.example.shoesmanagement.model.AuditorAwareImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.TimeZone;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaAuditing(auditorAwareRef = "aware")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] PUBLIC_URLS = {"/",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/**/*.png",
            "/**/*.gif",
            "/public/**",
            "/**/*.json",
            "/**/*.jpg",
            // enable swagger endpoints
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/configuration/security",
            "/manage/api-docs",
            "/consumer/signin",
            "/consumer/signup"};

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll()
                .antMatchers("/shoes/**", "/brand/**").permitAll()
                .anyRequest().authenticated();// All other request must be specify token

        // Custom JWT based security filter
        httpSecurity.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        // disable page caching
        httpSecurity.headers().cacheControl();
        httpSecurity.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public");
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuditorAware<String> aware() {
        return new AuditorAwareImpl();
    }
    @Autowired
    public void configureJackson(ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

}
