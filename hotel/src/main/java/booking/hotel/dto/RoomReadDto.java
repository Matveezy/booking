package booking.hotel.dto;

import booking.hotel.entity.RoomClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomReadDto {

    private long id;
    private long roomNumber;
    private RoomClass roomClass;
    private long price;
    private long hotelId;
}
