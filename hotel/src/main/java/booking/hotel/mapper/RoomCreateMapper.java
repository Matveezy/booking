package booking.hotel.mapper;

import booking.hotel.dto.RoomCreateDto;
import booking.hotel.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class RoomCreateMapper implements Mapper<RoomCreateDto, Room> {

    @Override
    public RoomCreateDto mapToDto(Room entity) {
        return null;
    }

    @Override
    public Room mapToEntity(RoomCreateDto dto) {
        return Room.builder()
                .roomClass(dto.getRoomClass())
                .roomNumber(dto.getRoomNumber())
                .price(dto.getPrice())
                .build();
    }
}
