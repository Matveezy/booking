package booking.hotel.dto;

import booking.hotel.entity.HotelClass;
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
public class HotelUpdateDto {

    @Min(value = 1, message = "hotel id value cannot be less than 1")
    private Long id;

    String name;

    private String city;

    @EnumNamePattern(regexp = "ONE_STAR|TWO_STARS|THREE_STARS|FOUR_STARS|FIVE_STARS",
            message = "hotelClass can take the following values: ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS")
    private HotelClass hotelClass;
}
