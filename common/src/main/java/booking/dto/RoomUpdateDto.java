package booking.dto;

import booking.validation.EnumNamePattern;
import booking.entity.RoomClass;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUpdateDto {
    @EnumNamePattern(regexp = "SINGLE|DOUBLE|TRIPLE|QUAD",
            message = "hotelClass can take the following values: SINGLE, DOUBLE, TRIPLE, QUAD")
    private RoomClass roomClass;
    @Min(value = 1, message = "room number cannot be less than 1")
    private long roomNumber;
    @Min(value = 0, message = "price value cannot be less than 0")
    private long price;
}
