package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, String> {

    @Query(value = "{creditCardNumber: ?0}")
    Wallet findWalletByCreditCard(String number);

    @Query(value = "{_id: ?0}")
    Wallet findWalletById(String id);

}