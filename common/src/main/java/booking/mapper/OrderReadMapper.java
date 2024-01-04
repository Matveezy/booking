package booking.mapper;

import booking.dto.OrderReadDto;
import booking.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    private final UserReadMapper userReadMapper;
    private final HotelReadMapper hotelReadMapper;
    private final RoomReadMapper roomReadMapper;

    @Override
    public OrderReadDto map(Order from) {
        return OrderReadDto.builder()
                .userReadDto(userReadMapper.map(from.getUser()))
                .dateIn(from.getDateIn())
                .dateOut(from.getDateOut())
                .hotel(hotelReadMapper.map(from.getHotel()))
                .room(roomReadMapper.map(from.getRoom()))
                .build();
    }
}