package booking.common.mapper;

import booking.common.exception.HotelNotFoundException;
import booking.common.repository.HotelRepository;
import booking.common.dto.RoomCreateDto;
import booking.common.entity.Hotel;
import booking.common.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomEntityMapper implements Mapper<RoomCreateDto, Room> {

    private final HotelRepository hotelRepository;

    @Override
    public Room map(RoomCreateDto roomCreateDto) {
        Optional<Hotel> optionalHotel = hotelRepository.findHotelById(roomCreateDto.getHotelId());
        if (optionalHotel.isEmpty()) throw new HotelNotFoundException(roomCreateDto.getHotelId());
        return Room.builder()
                .roomNumber(roomCreateDto.getRoomNumber())
                .roomClass(roomCreateDto.getRoomClass())
                .price(roomCreateDto.getPrice())
                .hotel(optionalHotel.get())
                .build();
    }
}
