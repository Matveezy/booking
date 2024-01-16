package booking.common.service;

import booking.common.exception.EntityNotFoundException;
import booking.common.exception.InsufficientMoneyBalanceException;
import booking.common.entity.Order;
import booking.common.repository.WalletRepository;
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
        var walletFrom = walletRepository.findWalletByUser_Id(fromUser);
        if (walletFrom.isEmpty())
            throw new EntityNotFoundException("Can't find user's balance with user id " + fromUser);
        var walletTo = walletRepository.findWalletByUser_Id(toUser);
        if (walletTo.isEmpty()) throw new EntityNotFoundException("Can't find user's balance with user id " + toUser);
        if (walletFrom.get().getBalance() < amount)
            throw new InsufficientMoneyBalanceException("User (id : " + fromUser + ") has " +
                                                        "not enough money for the transfer");
        walletFrom.get().setBalance(walletFrom.get().getBalance() - amount);
        walletTo.get().setBalance(walletTo.get().getBalance() + amount);

        walletRepository.save(walletFrom.get());
        walletRepository.save(walletTo.get());
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
        var user = userService.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        var wallet = walletRepository.findWalletByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet with user id " + user.getId() + " not found"));

        return wallet.getBalance();
    }
}
