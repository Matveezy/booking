package booking.hotel.dto;

import booking.hotel.validation.Dates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Dates
public class OrderInfoDto implements DateInOutDto {
    private Long userId;
    private Instant dateIn;
    private Instant dateOut;
    private HotelInfoDto hotel;
    private RoomReadDto room;

    @Override
    public Instant in() {
        return dateIn;
    }

    @Override
    public Instant out() {
        return dateOut;
    }
}
