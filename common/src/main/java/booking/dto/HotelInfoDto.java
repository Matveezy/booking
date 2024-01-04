package booking.dto;

import booking.entity.HotelClass;
import booking.validation.EnumNamePattern;
import jakarta.validation.constraints.Min;
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
public class HotelInfoDto {

    @Min(value = 1, message = "hotel id value cannot be less than 1")
    private Long id;

    @NotBlank(message = "hotel info cannot be blank!")
    private String name;

    @NotBlank(message = "city name cannot be blank!")
    private String city;

    @NotNull
    @EnumNamePattern(regexp = "ONE_STAR|TWO_STARS|THREE_STARS|FOUR_STARS|FIVE_STARS",
            message = "hotelClass can take the following values: ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS")
    private HotelClass hotelClass;
}
