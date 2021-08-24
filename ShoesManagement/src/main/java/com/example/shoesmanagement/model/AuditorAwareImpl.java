package com.example.shoesmanagement.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String>, Serializable {

    @Override
    public Optional<String> getCurrentAuditor() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            return Optional.of(name);
        }
        return Optional.empty();

    }
}
