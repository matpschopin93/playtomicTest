package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.PaymentRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class WalletService {

    private Logger log = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public Wallet newWallet(String name, String surname,String creditCardNumber){
        Wallet newWallet = new Wallet();
        newWallet.setSurname(surname);
        newWallet.setName(name);
        newWallet.setCreditCardNumber(creditCardNumber);
        newWallet.setAmount(BigDecimal.ZERO);
        Wallet check = walletRepository.findWalletByCreditCard(creditCardNumber);
        if(check==null){
            walletRepository.save(newWallet);
            log.info("Inserted new Account");
        }else{
            log.info("Cannot create User cause of Credit Card Used by another Account");
        }
        return newWallet;
    }

    public Payment charge(String creditCardNumber, BigDecimal amount) {
        Payment pay= new Payment();
        try {
            Wallet wallet = walletRepository.findWalletByCreditCard(creditCardNumber);
            wallet.setAmount(wallet.getAmount().add(amount));
            walletRepository.save(wallet);
            pay.setDate(new Date());
            pay.setIdWallet(wallet.get_id());
            pay.setCreditCardNumber(creditCardNumber);
            pay.setCharge(true);
            pay.setRefound(false);
            pay.setAmount(amount);
            paymentRepository.save(pay);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return pay;
    }
    public Wallet payment(String creditCardNumber, BigDecimal amount) {
            Wallet wallet = walletRepository.findWalletByCreditCard(creditCardNumber);
        try {
            if(amount.compareTo(BigDecimal.TEN)<=0||wallet.getAmount().compareTo(amount)<=0){
                throw new StripeAmountTooSmallException();
            }
            else{
                wallet.setAmount(wallet.getAmount().subtract(amount));
                walletRepository.save(wallet);
                Payment newPayment = new Payment();
                newPayment.setDate(new Date());
                newPayment.setIdWallet(wallet.get_id());
                newPayment.setCreditCardNumber(creditCardNumber);
                newPayment.setCharge(false);
                newPayment.setRefound(false);
                newPayment.setAmount(amount);
                paymentRepository.save(newPayment);
            }
        }catch(Exception e){
            log.error(e.toString());
        }
        return wallet;
    }

    public Payment refound(String transactionId) {
        Payment payment = paymentRepository.findTransaction(transactionId);
        Wallet wallet = walletRepository.findWalletById(payment.getIdWallet());
        wallet.setAmount(wallet.getAmount().add(payment.getAmount()));
        walletRepository.save(wallet);
        Payment newPayment = new Payment();
        newPayment.setDate(new Date());
        newPayment.setIdWallet(wallet.get_id());
        newPayment.setCreditCardNumber(payment.getCreditCardNumber());
        newPayment.setCharge(false);
        newPayment.setRefound(true);
        newPayment.setAmount(payment.getAmount());
        paymentRepository.save(newPayment);
        return newPayment;
    }
}
