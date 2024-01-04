package booking.dto;

import booking.entity.RoomClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomInfoDto {

    private long id;
    private long roomNumber;
    private RoomClass roomClass;
    private long price;
    private HotelInfoDto hotelInfoDto;
}
