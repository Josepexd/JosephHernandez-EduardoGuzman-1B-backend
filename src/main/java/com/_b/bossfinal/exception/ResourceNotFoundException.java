package com._b.bossfinal.exception;

// La lanzo cuando busco algo por ID y no existe; el handler la convierte en un 404
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
