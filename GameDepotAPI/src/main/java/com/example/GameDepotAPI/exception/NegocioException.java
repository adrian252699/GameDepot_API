package com.example.GameDepotAPI.exception;

public class NegocioException extends RuntimeException {
    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
