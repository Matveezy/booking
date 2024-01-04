package booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class CreateOrderDto {

    long userId;
    Instant dateIn;
    Instant dateOut;
    long roomId;
}
