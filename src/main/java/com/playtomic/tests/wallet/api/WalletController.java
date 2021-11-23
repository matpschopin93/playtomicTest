package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.PaymentRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
public class WalletController {

    private final WalletRepository walletRepository;
    private final PaymentRepository paymentRepository;

    WalletController(WalletRepository walletRepository,PaymentRepository paymentRepository) {
        this.walletRepository = walletRepository;
        this.paymentRepository = paymentRepository;
    }

    @Autowired
    StripeService stripeService;

    @Autowired
    WalletService walletService;

    private Logger log = LoggerFactory.getLogger(WalletController.class);

    @RequestMapping("/insertNewWallet")
    void insertWallet(@RequestParam String name, @RequestParam String surname,@RequestParam String creditCardNumber) {
        walletService.newWallet(name,surname,creditCardNumber);
    }

    @RequestMapping("/charges")
    void rechargeWallet(@RequestParam String creditCardNumber, @RequestParam BigDecimal amount) {
        try {
            stripeService.charge(creditCardNumber, amount);
            walletService.charge(creditCardNumber, amount);
            log.info("Charge of Wallet Done");
        }catch(Exception e){
            log.error(e.toString());
        }
    }
    @RequestMapping("/payment")
    void dopayment(@RequestParam String creditCardNumber, @RequestParam BigDecimal amount) {
        try {

            walletService.payment(creditCardNumber,amount);

            log.info("Payment done");
        }catch(Exception e){
            log.error(e.toString());
        }
    }
    @RequestMapping("/refound")
    void refound(@RequestParam String transactionId) {

        try {
            Payment payment = paymentRepository.findTransaction(transactionId);
            if(payment!=null){
                stripeService.refund(transactionId);
                walletService.refound(transactionId);
                log.info("Refound of Payment Done");
            }
        }catch(Exception e){
            log.error(e.toString());
        }
    }

    @RequestMapping("/getUserAmount")
    Wallet checkAmountWallet(@RequestParam String id) {
        Wallet wallet = walletRepository.findWalletById(id);
        log.info("The amount is: "+wallet.getAmount());
        return wallet;
    }

    @GetMapping("/getUserTransaction")
    List<Payment> all(@RequestParam String creditCardNumber) {
        return paymentRepository.findTransactionOfUserByCreditCard(creditCardNumber);
    }

    @PostMapping("/insertWallets")
    Wallet newWallet(@RequestBody Wallet newWallet) {
        return walletRepository.save(newWallet);
    }

    @DeleteMapping("/wallets/{id}")
    void deleteWallet(@PathVariable String id) {
        walletRepository.deleteById(id);
    }

}
