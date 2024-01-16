package booking.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyTransferResponse {

    boolean isCompleted;
    String cause;
}
