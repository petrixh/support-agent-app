package com.example.security;

import com.example.domain.Agent;
import com.example.repository.AgentRepository;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.ui.LoginView;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(AgentRepository agents) {
        return username -> {
            Agent agent = agents.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
            return User.withUsername(agent.getUsername())
                .password(agent.getPasswordHash())
                .disabled(!agent.isActive())
                .roles("AGENT")
                .build();
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.with(VaadinSecurityConfigurer.vaadin(), cfg -> cfg.loginView(LoginView.class));
        return http.build();
    }
}
