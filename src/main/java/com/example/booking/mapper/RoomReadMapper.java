package com.example.booking.mapper;

import com.example.booking.dto.RoomInfoDto;
import com.example.booking.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomReadMapper implements Mapper<Room, RoomInfoDto> {

    private OrderReadMapper orderReadMapper;
    private HotelReadMapper hotelReadMapper;

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

    public void setOrderReadMapper(OrderReadMapper orderReadMapper) {
        this.orderReadMapper = orderReadMapper;
    }

    public void setHotelReadMapper(HotelReadMapper hotelReadMapper) {
        this.hotelReadMapper = hotelReadMapper;
    }
}