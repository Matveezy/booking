package booking.common.mapper;

import booking.common.dto.RoomInfoDto;
import booking.common.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomReadMapper implements Mapper<Room, RoomInfoDto> {

    private HotelReadMapper hotelReadMapper;

    @Override
    public RoomInfoDto map(Room from) {
        return RoomInfoDto
                .builder()
                .id(from.getId())
                .price(from.getPrice())
                .roomClass(from.getRoomClass())
                .hotel(hotelReadMapper.map(from.getHotel()))
                .roomNumber(from.getRoomNumber())
                .build();
    }

    @Autowired
    public void setHotelReadMapper(HotelReadMapper hotelReadMapper) {
        this.hotelReadMapper = hotelReadMapper;
    }
}