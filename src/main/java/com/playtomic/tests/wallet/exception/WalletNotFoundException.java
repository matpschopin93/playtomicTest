package com.playtomic.tests.wallet.exception;


public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String id) {
        super("Could not find employee " + id);
    }
}
