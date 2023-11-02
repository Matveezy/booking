package com.example.booking.mapper;

import com.example.booking.dto.RoomInfoDto;
import com.example.booking.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomReadMapper implements Mapper<Room, RoomInfoDto> {

    private final OrderReadMapper orderReadMapper;
    private final HotelReadMapper hotelReadMapper;

    @Override
    public RoomInfoDto map(Room from) {
        return RoomInfoDto
                .builder()
                .price(from.getPrice())
                .roomClass(from.getRoomClass())
                .hotelInfoDto(hotelReadMapper.map(from.getHotel()))
                .ordersThisRoom(
                        from.getOrdersThisRoom().stream()
                                .map(orderReadMapper::map)
                                .toList()
                )
                .roomNumber(from.getRoomNumber())
                .build();
    }
}