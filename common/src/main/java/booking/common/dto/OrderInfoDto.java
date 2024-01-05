package booking.common.dto;

import booking.common.validation.Dates;
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
    private UserInfoDto user;
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