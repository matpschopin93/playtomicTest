package com.playtomic.tests.wallet.service;

public class StripeServiceException extends RuntimeException {

    public StripeServiceException() {
        super();
    }

    public StripeServiceException(String message) {
        super(message);
    }
}
