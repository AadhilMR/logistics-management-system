package com.aadhil.ejb.exception;

public class CargoCreationFailedException extends RuntimeException {
    public CargoCreationFailedException(String message) {
        super(message);
    }
}
