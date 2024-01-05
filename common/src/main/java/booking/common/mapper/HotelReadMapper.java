package booking.common.mapper;

import booking.common.dto.HotelInfoDto;
import booking.common.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelReadMapper implements Mapper<Hotel, HotelInfoDto> {

    @Override
    public HotelInfoDto map(Hotel from) {
        return HotelInfoDto.builder()
                .id(from.getId())
                .name(from.getName())
                .hotelClass(from.getHotelClass())
                .city(from.getCity())
                .build();
    }
}