package com.bcss.checador.auth;

import com.bcss.checador.domain.Usuario;
import com.bcss.checador.service.JwtService;
import com.bcss.checador.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioService  usuarioService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.usuarioService = usuarioService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if(request.getServletPath().contains("/auth")){
            filterChain.doFilter(request,response);
            return;
        }

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        final String token = authorizationHeader.substring(7);
        final String email = jwtService.extractUsername(token);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(email == null || authentication == null){
            filterChain.doFilter(request,response);
            return;
        }

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        final boolean isTokenExpiredOrRevoked = jwtService.findByToken(token)
                .map(jwt -> !jwt.getIsExpired() && !jwt.getIsRevoked())
                .orElse(false);

        if(isTokenExpiredOrRevoked){
            final Optional<Usuario> user = usuarioService.findByEmail(email);

            if(user.isPresent()){
                final boolean isTokenValid = jwtService.isTokenValid(token, user.get());

                if(isTokenValid){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
