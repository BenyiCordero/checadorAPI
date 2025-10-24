package com.bcss.checador.exception;

public class QrCodeNotFoundException extends RuntimeException {
    public QrCodeNotFoundException() {
        super("Codigo QR no encontrado");
    }
}
