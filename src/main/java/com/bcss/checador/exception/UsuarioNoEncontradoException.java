package com.bcss.checador.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado");
    }
}
