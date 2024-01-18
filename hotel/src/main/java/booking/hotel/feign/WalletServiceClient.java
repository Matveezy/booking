package booking.hotel.feign;

import booking.hotel.dto.MoneyTransferRequest;
import booking.hotel.dto.MoneyTransferResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet")
public interface WalletServiceClient {

    @PutMapping("/transfer")
    ResponseEntity<MoneyTransferResponse> transferMoney(@RequestBody MoneyTransferRequest moneyTransferRequest);
}
