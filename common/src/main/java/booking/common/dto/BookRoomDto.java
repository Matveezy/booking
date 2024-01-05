package booking.common.dto;

import booking.common.validation.Dates;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.Instant;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Dates
public class BookRoomDto implements DateInOutDto {

    @Min(value = 1, message = "ownerId value cannot be less than 1")
    private long userId; //todo get user id by jwt

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
