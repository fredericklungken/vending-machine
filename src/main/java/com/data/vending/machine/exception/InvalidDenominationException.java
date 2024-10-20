package com.data.vending.machine.exception;

public class InvalidDenominationException extends RuntimeException {
    public InvalidDenominationException(String message) {
        super(message);
    }
}

