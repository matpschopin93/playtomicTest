package com.playtomic.tests.wallet.service;

public class StripeAmountTooSmallException extends StripeServiceException {

    public StripeAmountTooSmallException() {
        new StripeServiceException("AmountTooSmall, needs recharge");
    }

}
