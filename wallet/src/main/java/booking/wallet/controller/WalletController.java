package booking.wallet.controller;

import booking.wallet.dto.UpdateBalanceRequest;
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
        ).block());
    }

    @PostMapping("/wallets")
    public ResponseEntity<?> createWallet(@RequestParam Long userId) {
        walletService.createWallet(userId).block();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/balance")
    public ResponseEntity<?> updateBalance(@RequestBody UpdateBalanceRequest updateBalanceRequest) {
        walletService.updateBalance(updateBalanceRequest.getUserId(), updateBalanceRequest.getAmount()).block();
        return ResponseEntity.ok().build();
    }
}
