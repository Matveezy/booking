package booking.mapper;

import booking.exception.HotelNotFoundException;
import booking.repository.HotelRepository;
import booking.dto.RoomCreateDto;
import booking.entity.Hotel;
import booking.entity.Room;
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
