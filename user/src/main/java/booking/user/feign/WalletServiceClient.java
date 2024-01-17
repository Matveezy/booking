package booking.user.feign;

import booking.user.dto.UpdateBalanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "WALLET-SERVICE")
public interface WalletServiceClient {

    @PostMapping("/wallets")
    ResponseEntity<?> createWallet(@RequestParam Long userId);

    @PutMapping("/balance")
    ResponseEntity<?> updateBalance(@RequestBody UpdateBalanceRequest updateBalanceRequest);
}
