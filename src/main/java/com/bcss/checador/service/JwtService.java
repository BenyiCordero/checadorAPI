package com.bcss.checador.service;

import com.bcss.checador.domain.Usuario;

import javax.crypto.SecretKey;
import java.util.Date;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(final Usuario usuario); // Cambiado a UserDetails
    String generateRefreshToken(final Usuario usuario); // Cambiado a UserDetails
    String buildToken(final Usuario usuario, final long expiration); // Cambiado a UserDetails
    boolean isTokenValid(String token, Usuario usuario); // Cambiado a UserDetails
    boolean isTokenExpired(String token);
    Date extractExpiration(String token);
    SecretKey getSignInKey();
}
