package booking.user.feign;

import booking.user.dto.UpdateBalanceRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wallet")
@CircuitBreaker(name = "walletServiceBreaker")
public interface WalletServiceClient {

    @PostMapping("/wallets")
    ResponseEntity<?> createWallet(@RequestParam Long userId);

    @PutMapping("/balance")
    ResponseEntity<?> updateBalance(@RequestBody UpdateBalanceRequest updateBalanceRequest);
}
