package com.bcss.checador.dto.request;

public record AuthRequest(
        String email,
        String password
) {
}
