package com.bcss.checador.service;

import com.bcss.checador.domain.Usuario;
import com.bcss.checador.dto.request.AuthRequest;
import com.bcss.checador.dto.request.RegisterRequest;
import com.bcss.checador.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse register(RegisterRequest registerRequest);
    TokenResponse login(AuthRequest authRequest);
    void saveUserToken(Usuario usuario, String token);
    void removeAllUserTokens(Usuario usuario);
    TokenResponse refreshToken(String authentication);
}