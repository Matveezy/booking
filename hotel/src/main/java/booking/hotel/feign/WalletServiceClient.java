package booking.hotel.feign;

import booking.hotel.dto.MoneyTransferRequest;
import booking.hotel.dto.MoneyTransferResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name = "wallet")
@CircuitBreaker(name = "walletServiceBreaker")
public interface WalletServiceClient {

    @PutMapping("/transfer")
    ResponseEntity<MoneyTransferResponse> transferMoney(@RequestBody MoneyTransferRequest moneyTransferRequest);
}
