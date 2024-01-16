package booking.hotel.mapper;

import booking.hotel.dto.RoomReadDto;
import booking.hotel.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomReadMapper implements Mapper<RoomReadDto, Room>{

    @Override
    public RoomReadDto mapToDto(Room entity) {
        return RoomReadDto
                .builder()
                .id(entity.getId())
                .roomClass(entity.getRoomClass())
                .price(entity.getPrice())
                .roomNumber(entity.getRoomNumber())
                .hotelId(entity.getHotel().getId())
                .build();
    }
}
