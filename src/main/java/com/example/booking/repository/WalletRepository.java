package com.example.booking.repository;

import com.example.booking.entity.User;
import com.example.booking.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findWalletByUser_Id(long id);
}
