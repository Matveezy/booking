package booking.hotel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrderInfoAlertDto {
    private UserInfoAlertDto user;
    private HotelInfoAlertDto hotel;
    private Instant dateIn;
    private Instant dateOut;
}
