package com.bcss.checador.dto.request;

import com.bcss.checador.auth.Rol;

public record RegisterRequest(
    String nombre,
    String primerApellido,
    String segundoApellido,
    String numeroTelefono,
    String email,
    String password,
    Rol rol
) {
}
