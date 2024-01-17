package booking.wallet.service;

import booking.wallet.dto.MoneyTransferResponse;
import booking.wallet.entity.Wallet;
import booking.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final Long DEFAULT_CREATED_USER_BALANCE = 0L;

    @Transactional
    public Mono<MoneyTransferResponse> transferMoney(long fromUserId, long toUserId, long amount) {
        Mono<Wallet> walletFromUser = walletRepository.findWalletByUserid(fromUserId);
        Mono<Wallet> walletToUser = walletRepository.findWalletByUserid(toUserId);
        return walletFromUser.zipWith(walletToUser)
                .flatMap(balances -> {
                    Wallet fromUserWallet = balances.getT1();
                    Wallet toUserWallet = balances.getT2();
                    if (fromUserWallet.getBalance() < amount) {
                        return Mono.just(MoneyTransferResponse.builder()
                                .isCompleted(false)
                                .cause("Not enough money to order")
                                .build());
                    }
                    fromUserWallet.setBalance(fromUserWallet.getBalance() - amount);
                    toUserWallet.setBalance(toUserWallet.getBalance() + amount);
                    Mono<Wallet> updateFromUserBalance = walletRepository.save(fromUserWallet);
                    Mono<Wallet> updateToUserBalance = walletRepository.save(toUserWallet);
                    return Mono.when(updateToUserBalance, updateFromUserBalance)
                            .thenReturn(MoneyTransferResponse.builder()
                                    .isCompleted(true)
                                    .build());
                });
    }

    @Transactional
    public Mono<Wallet> createWallet(Long userId) {
        Wallet walletEntity = Wallet.builder()
                .balance(DEFAULT_CREATED_USER_BALANCE)
                .userid(userId)
                .build();
        return walletRepository.save(walletEntity);
    }

    @Transactional
    public Mono<Wallet> updateBalance(Long userId, long amount) {
        return walletRepository.findWalletByUserid(userId)
                .map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalWallet -> {
                    if (optionalWallet.isPresent()) {
                        optionalWallet.get().setBalance(optionalWallet.get().getBalance() + amount);
                        return walletRepository.save(optionalWallet.get());
                    }
                    return Mono.empty();
                });
    }
}
