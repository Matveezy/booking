package com.example.booking.service;

import com.example.booking.entity.Order;
import com.example.booking.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserService userService;

    @Transactional
    public void transferMoney(long fromUser, long toUser, long amount) {
        var walletFrom = walletRepository.findWalletByUser_Id(fromUser)
                .orElseThrow(() -> new IllegalArgumentException("Wallet with user id " + fromUser + " not found"));
        var walletTo = walletRepository.findWalletByUser_Id(toUser)
                .orElseThrow(() -> new IllegalArgumentException("Wallet with user id " + toUser + " not found"));

        walletFrom.setBalance(walletFrom.getBalance() - amount);
        walletTo.setBalance(walletTo.getBalance() + amount);

        walletRepository.save(walletFrom);
        walletRepository.save(walletTo);
    }

    @Transactional
    public void bookRoom(Order order) {
        var fromUser = order.getUser().getId();
        var toUser = order.getHotel().getOwner().getId();
        var days = Duration.between(order.getDateIn(), order.getDateOut()).getSeconds() / (60 * 60 * 24);
        var amount = order.getRoom().getPrice() * days;
        transferMoney(fromUser, toUser, amount);
    }

    @Transactional
    public void cancelRoom(Order order) {
        var toUser = order.getUser().getId();
        var fromUser = order.getHotel().getOwner().getId();
        var days = Duration.between(order.getDateIn(), order.getDateOut()).getSeconds() / (60 * 60 * 24);
        var amount = order.getRoom().getPrice() * days;
        transferMoney(fromUser, toUser, amount);
    }

    public long getUserBalance(long id) {
        var user = userService.findUserEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        var wallet = walletRepository.findWalletByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet with user id " + user.getId() + " not found"));

        return wallet.getBalance();
    }
}
