package booking.dto;

import booking.validation.Dates;
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
public class OrderReadDto implements DateInOutDto {
    private UserReadDto userReadDto;
    private Instant dateIn;
    private Instant dateOut;
    private HotelInfoDto hotel;
    private RoomInfoDto room;

    @Override
    public Instant in() {
        return dateIn;
    }

    @Override
    public Instant out() {
        return dateOut;
    }
}
