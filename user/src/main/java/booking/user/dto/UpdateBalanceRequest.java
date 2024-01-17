package booking.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBalanceRequest {

    long userId;
    long amount;
}
