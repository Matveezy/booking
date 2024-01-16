package booking.wallet.controller;

import booking.wallet.service.WalletService;
import booking.wallet.dto.MoneyTransferRequest;
import booking.wallet.dto.MoneyTransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PutMapping("/transfer")
    public ResponseEntity<MoneyTransferResponse> transferMoney(@RequestBody MoneyTransferRequest moneyTransferRequest) {
        return ResponseEntity.ok(walletService.transferMoney(
                moneyTransferRequest.getFromUserId(),
                moneyTransferRequest.getToUserId(),
                moneyTransferRequest.getAmount()
        ));
    }

    @PostMapping("/wallets")
    public ResponseEntity<?> createWallet(@RequestParam Long userId) {
        walletService.createWallet(userId);
        return ResponseEntity.ok().build();
    }
}
