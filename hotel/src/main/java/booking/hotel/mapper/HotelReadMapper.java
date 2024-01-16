package booking.hotel.mapper;

import booking.hotel.dto.HotelInfoDto;
import booking.hotel.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelReadMapper implements Mapper<HotelInfoDto, Hotel> {

    @Override
    public HotelInfoDto mapToDto(Hotel entity) {
        return HotelInfoDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .hotelClass(entity.getHotelClass())
                .ownerId(entity.getUserId())
                .build();
    }
}
