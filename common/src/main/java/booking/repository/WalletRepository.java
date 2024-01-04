package booking.repository;

import booking.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findWalletByUser_Id(long id);
}
