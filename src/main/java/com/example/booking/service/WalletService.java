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
    public boolean transferMoney(long fromUser, long toUser, long amount) {
        var walletFrom = walletRepository.findWalletByUser_Id(fromUser);
        if (walletFrom.isEmpty()) return false;
        var walletTo = walletRepository.findWalletByUser_Id(toUser);
        if (walletTo.isEmpty()) return false;
        walletFrom.get().setBalance(walletFrom.get().getBalance() - amount);
        walletTo.get().setBalance(walletTo.get().getBalance() + amount);

        walletRepository.save(walletFrom.get());
        walletRepository.save(walletTo.get());
        return true;
    }


    @Transactional
    public boolean bookRoom(Order order) {
        var fromUser = order.getUser().getId();
        var toUser = order.getHotel().getOwner().getId();
        var days = Duration.between(order.getDateIn(), order.getDateOut()).getSeconds() / (60 * 60 * 24);
        var amount = order.getRoom().getPrice() * days;
        return transferMoney(fromUser, toUser, amount);
    }


    @Transactional
    public boolean cancelRoom(Order order) {
        var toUser = order.getUser().getId();
        var fromUser = order.getHotel().getOwner().getId();
        var days = Duration.between(order.getDateIn(), order.getDateOut()).getSeconds() / (60 * 60 * 24);
        var amount = order.getRoom().getPrice() * days;
        return transferMoney(fromUser, toUser, amount);
    }

    public long getUserBalance(long id) {
        var user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        var wallet = walletRepository.findWalletByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet with user id " + user.getId() + " not found"));

        return wallet.getBalance();
    }
}
