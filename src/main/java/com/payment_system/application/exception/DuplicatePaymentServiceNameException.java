package com.payment_system.application.exception;

public class DuplicatePaymentServiceNameException extends RuntimeException {
    public DuplicatePaymentServiceNameException(String message) {
        super(message);
    }
}
