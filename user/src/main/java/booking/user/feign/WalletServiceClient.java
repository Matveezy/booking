package booking.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "WALLET-SERVICE")
public interface WalletServiceClient {

    @PostMapping("/wallets")
    ResponseEntity<?> createWallet(@RequestParam Long userId);
}
