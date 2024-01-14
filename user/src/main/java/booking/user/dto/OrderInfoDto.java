package booking.user.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class OrderInfoDto {
    private UserInfoDto user;
    private HotelInfoDto hotel;
    private Instant dateIn;
    private Instant dateOut;
}
