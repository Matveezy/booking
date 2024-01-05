package booking.common.dto;

import booking.common.validation.Dates;
import booking.common.entity.RoomClass;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Dates
public class RoomsFilterSearchDto implements DateInOutDto {
    private RoomClass roomClass;
    private Instant dateIn;
    private Instant dateOut;

    @Override
    public Instant in() {
        return dateIn;
    }

    @Override
    public Instant out() {
        return dateOut;
    }
}
