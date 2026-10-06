package com._b.bossfinal.exception;

// La lanzo cuando se rompe una regla de negocio (capacidad, fecha ocupada, email repetido...); el handler la convierte en un 400
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
