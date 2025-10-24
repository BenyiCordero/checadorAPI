package com.bcss.checador.exception;

public class CredencialesInvalidasException extends RuntimeException {
  public CredencialesInvalidasException() {
    super("Credenciales invalidas");
  }
}
