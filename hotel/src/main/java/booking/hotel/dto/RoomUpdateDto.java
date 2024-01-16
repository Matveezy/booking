package booking.hotel.dto;

import booking.hotel.entity.RoomClass;
import booking.hotel.validation.EnumNamePattern;
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

    @Min(value = 1, message = "hotel id value cannot be less than 1")
    private Long id;

    @EnumNamePattern(regexp = "SINGLE|DOUBLE|TRIPLE|QUAD",
            message = "hotelClass can take the following values: SINGLE, DOUBLE, TRIPLE, QUAD")
    private RoomClass roomClass;
    @Min(value = 1, message = "room number cannot be less than 1")

    private Long roomNumber;

    @Min(value = 0, message = "price value cannot be less than 0")
    private Long price;
}
