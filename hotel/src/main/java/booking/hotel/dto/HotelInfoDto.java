package booking.hotel.dto;

import booking.hotel.entity.HotelClass;
import booking.hotel.validation.EnumNamePattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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

    private List<Long> ownerId;
}
