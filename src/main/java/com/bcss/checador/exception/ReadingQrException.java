package com.bcss.checador.exception;

public class ReadingQrException extends RuntimeException {
    public ReadingQrException() {
        super("No se pudo leer el codigo qr");
    }
}
