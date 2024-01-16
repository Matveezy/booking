package booking.wallet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyTransferRequest {

    long fromUserId;
    long toUserId;
    long amount;
}
