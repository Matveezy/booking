package booking.common.mapper;

import booking.common.dto.OrderInfoDto;
import booking.common.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderInfoDto> {

    private final UserReadMapper userReadMapper;
    private final HotelReadMapper hotelReadMapper;
    private final RoomReadMapper roomReadMapper;

    @Override
    public OrderInfoDto map(Order from) {
        return OrderInfoDto.builder()
                .user(userReadMapper.map(from.getUser()))
                .dateIn(from.getDateIn())
                .dateOut(from.getDateOut())
                .hotel(hotelReadMapper.map(from.getHotel()))
                .room(roomReadMapper.map(from.getRoom()))
                .build();
    }
}