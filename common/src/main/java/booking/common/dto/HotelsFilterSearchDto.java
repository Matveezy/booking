package booking.common.dto;

import booking.common.entity.HotelClass;
import lombok.*;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelsFilterSearchDto {
    private String city;
    private HotelClass hotelClass;
}
