package booking.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelInfoAlertDto {
    private String name;
    private String city;
}
