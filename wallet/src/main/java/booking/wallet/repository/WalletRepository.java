package booking.wallet.repository;

import booking.wallet.entity.Wallet;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface WalletRepository extends R2dbcRepository<Wallet, Long> {
    Mono<Wallet> findWalletByUserid(long id);
}
