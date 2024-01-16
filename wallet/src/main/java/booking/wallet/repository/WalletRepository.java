package booking.wallet.repository;

import booking.wallet.entity.Wallet;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findWalletByUserId(long id);
}
