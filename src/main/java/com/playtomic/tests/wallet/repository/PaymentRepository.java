package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    @Query(value = "{idWallet: ?0}")
    List<Payment> findTransactionOfUserByCreditCard(String id);

    @Query(value = "{_id: ?0}")
    Payment findTransaction(String id);

}
