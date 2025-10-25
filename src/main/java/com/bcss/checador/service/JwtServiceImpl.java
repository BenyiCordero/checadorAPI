package com.bcss.checador.service;

import com.bcss.checador.domain.Token;
import com.bcss.checador.domain.Usuario;
import com.bcss.checador.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    private final TokenRepository tokenRepository;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public JwtServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String extractUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public String generateToken(Usuario usuario) {
        return buildToken(usuario, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(Usuario usuario) {
        return buildToken(usuario, refreshExpiration);
    }

    @Override
    public String buildToken(Usuario usuario, long expiration) {
        return Jwts
                .builder()
                .claims(Map.of("name", usuario.getNombre()))
                .subject(usuario.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, Usuario usuario) {
        String username =extractUsername(token);
        return (username.equals(usuario.getEmail()) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    @Override
    public SecretKey getSignInKey() {
        final byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public List<Token> findAllTokensByUsuario(Usuario usuario) {
        return tokenRepository.findByUser(usuario);
    }

    @Override
    public void deleteTokens(List<Token> tokens) {
        tokenRepository.deleteAll(tokens);
    }
}
