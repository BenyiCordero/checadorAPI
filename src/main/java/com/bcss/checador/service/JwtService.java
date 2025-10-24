package com.bcss.checador.service;

import com.bcss.checador.domain.Token;
import com.bcss.checador.domain.Usuario;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(final Usuario usuario); // Cambiado a UserDetails
    String generateRefreshToken(final Usuario usuario); // Cambiado a UserDetails
    String buildToken(final Usuario usuario, final long expiration); // Cambiado a UserDetails
    boolean isTokenValid(String token, Usuario usuario); // Cambiado a UserDetails
    boolean isTokenExpired(String token);
    Date extractExpiration(String token);
    SecretKey getSignInKey();
    Optional<Token> findByToken(String token);
    void saveToken(Token token);
    List<Token> findAllTokensByUsuario(Usuario usuario);
    void deleteTokens(List<Token> tokens);
}
