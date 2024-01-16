package booking.hotel.mapper;

import booking.hotel.dto.HotelCreateDto;
import booking.hotel.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelCreateMapper implements Mapper<HotelCreateDto, Hotel>{

    @Override
    public HotelCreateDto mapToDto(Hotel entity) {
        return null;
    }

    @Override
    public Hotel mapToEntity(HotelCreateDto dto) {
        return Hotel.builder()
                .city(dto.getCity())
                .hotelClass(dto.getHotelClass())
                .name(dto.getName())
                .build();
    }
}
