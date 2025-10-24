package com.bcss.checador.service;

import com.bcss.checador.domain.Token;
import com.bcss.checador.domain.Usuario;
import com.bcss.checador.dto.request.AuthRequest;
import com.bcss.checador.dto.request.RegisterRequest;
import com.bcss.checador.dto.response.TokenResponse;
import com.bcss.checador.exception.CredencialesInvalidasException;
import com.bcss.checador.exception.FormatoTokenInvalidoException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(JwtService jwtService, UsuarioService usuarioService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    @Override
    public TokenResponse register(RegisterRequest registerRequest) {
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.nombre());
        usuario.setPrimerApellido(registerRequest.primerApellido());
        usuario.setSegundoApellido(registerRequest.segundoApellido());
        usuario.setNumeroTelefono(registerRequest.numeroTelefono());
        usuario.setEmail(registerRequest.email());
        usuario.setPassword(passwordEncoder.encode(registerRequest.password()));
        usuario.setRol(registerRequest.rol());
        usuarioService.saveIfNotExist(usuario);

        String token = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        saveUserToken(usuario, token);
        return new TokenResponse(token, refreshToken);
    }

    @Transactional
    @Override
    public TokenResponse login(AuthRequest authRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.email(), authRequest.password()
                    )
            );
        } catch (CredencialesInvalidasException e) {
            throw new CredencialesInvalidasException();
        }

        Optional<Usuario> usuario = usuarioService.findByEmail(authRequest.email());
        String accessToken = jwtService.generateToken(usuario.get());
        String refreshToken = jwtService.generateRefreshToken(usuario.get());
        removeAllUserTokens(usuario.get());
        saveUserToken(usuario.get(), accessToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void saveUserToken(Usuario usuario, String token) {
        Token tokenJwt = new Token();
        tokenJwt.setToken(token);
        tokenJwt.setIsExpired(false);
        tokenJwt.setIsRevoked(false);
        tokenJwt.setTokenType(Token.TokenType.BEARER);
        tokenJwt.setUsuario(usuario);
        jwtService.saveToken(tokenJwt);
    }

    @Override
    public void removeAllUserTokens(Usuario usuario) {
        List<Token> tokens = jwtService.findAllTokensByUsuario(usuario);
        if(tokens.isEmpty()){
            jwtService.deleteTokens(tokens);
        }
    }

    @Override
    public TokenResponse refreshToken(String authentication) {

        if (authentication == null || !authentication.startsWith("Bearer ")) {
            throw new FormatoTokenInvalidoException();
        }
        String refreshToken = authentication.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            return null;
        }

        Usuario usuario = usuarioService.findByEmail(userEmail).orElseThrow();
        boolean isTokenValid = jwtService.isTokenValid(refreshToken, usuario);
        if (!isTokenValid) {
            return null;
        }

        String accessToken = jwtService.generateRefreshToken(usuario);
        removeAllUserTokens(usuario);
        saveUserToken(usuario, accessToken);

        return new TokenResponse(accessToken, refreshToken);
    }

}
