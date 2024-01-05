package booking.common.dto;

import booking.common.validation.EnumNamePattern;
import booking.common.entity.HotelClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelCreateDto {
    @NotBlank(message = "hotel name cannot be blank")
    private String name;
    @NotBlank(message = "city name cannot be blank")
    private String city;
    @NotNull
    @EnumNamePattern(regexp = "ONE_STAR|TWO_STARS|THREE_STARS|FOUR_STARS|FIVE_STARS",
            message = "hotelClass can take the following values: ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS")
    private HotelClass hotelClass;
}
