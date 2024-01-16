package booking.wallet.service;

import booking.wallet.dto.MoneyTransferResponse;
import booking.wallet.entity.Wallet;
import booking.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final Long DEFAULT_CREATED_USER_BALANCE = 0L;

    @Transactional
    public MoneyTransferResponse transferMoney(long fromUserId, long toUserId, long amount) {
        Optional<Wallet> fromUserWalletOptional = walletRepository.findWalletByUserId(fromUserId);
        Optional<Wallet> toUserWalletOptional = walletRepository.findWalletByUserId(toUserId);
        if (fromUserWalletOptional.isEmpty() || toUserWalletOptional.isEmpty()) {
            return MoneyTransferResponse.builder()
                    .isCompleted(false)
                    .cause("No wallet entity")
                    .build();
        }
        Wallet fromUserWallet = fromUserWalletOptional.get();
        Wallet toUserWallet = toUserWalletOptional.get();
        if (fromUserWallet.getBalance() < amount) {
            return MoneyTransferResponse.builder()
                    .isCompleted(false)
                    .cause("Not enough money to order")
                    .build();
        }

        fromUserWallet.setBalance(fromUserWallet.getBalance() - amount);
        toUserWallet.setBalance(toUserWallet.getBalance() + amount);
        walletRepository.save(fromUserWallet);
        walletRepository.save(toUserWallet);
        return MoneyTransferResponse.builder()
                .isCompleted(true)
                .build();
    }

    @Transactional
    public void createWallet(Long userId) {
        Wallet walletEntity = Wallet.builder()
                .balance(DEFAULT_CREATED_USER_BALANCE)
                .userId(userId)
                .build();
        walletRepository.save(walletEntity);
    }

    @Transactional
    public Long updateBalance(Long userId, long amount) {
        Optional<Wallet> walletByUserId = walletRepository.findWalletByUserId(userId);
        return walletByUserId.map(wallet -> {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
            return wallet.getBalance();
        }).orElseThrow(() -> new EntityNotFoundException());
    }
}
