package booking.hotel.mapper;

import booking.hotel.dto.OrderInfoDto;
import booking.hotel.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<OrderInfoDto, Order>{

    private final RoomReadMapper roomReadMapper;
    private final HotelReadMapper hotelReadMapper;

    @Override
    public OrderInfoDto mapToDto(Order entity) {
        return OrderInfoDto.builder()
                .room(roomReadMapper.mapToDto(entity.getRoom()))
                .userId(entity.getUserId())
                .hotel(hotelReadMapper.mapToDto(entity.getHotel()))
                .dateIn(entity.getDateIn())
                .dateOut(entity.getDateOut())
                .build();
    }
}
