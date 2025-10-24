package com.bcss.checador.config;

import com.bcss.checador.domain.Usuario;
import com.bcss.checador.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    private final UsuarioService usuarioService;

    public AppConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final Usuario usuario = usuarioService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(usuario.getEmail())
                    .password(usuario.getPassword())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
