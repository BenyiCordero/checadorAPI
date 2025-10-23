package com.bcss.checador.exception;

public class EmailRepetidoException extends RuntimeException {
    public EmailRepetidoException() {
        super("El email ya existe");
    }
}
