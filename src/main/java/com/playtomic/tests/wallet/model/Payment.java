package com.playtomic.tests.wallet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document
public class Payment  {


    @Id
    public String _id;

    public String idWallet;

    public String creditCardNumber;

    public BigDecimal amount;

    public boolean isRefound() {
        return refound;
    }

    public void setRefound(boolean refound) {
        this.refound = refound;
    }

    boolean refound;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date date;

    boolean charge;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isCharge() {
        return charge;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public String getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(String idWallet) {
        this.idWallet = idWallet;
    }

}
