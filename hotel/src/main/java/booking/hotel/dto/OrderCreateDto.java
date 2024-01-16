package booking.hotel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class OrderCreateDto {

    Instant dateIn;
    Instant dateOut;
    long roomId;
}
