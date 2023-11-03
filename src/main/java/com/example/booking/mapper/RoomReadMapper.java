package com.example.booking.mapper;

import com.example.booking.dto.RoomInfoDto;
import com.example.booking.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomReadMapper implements Mapper<Room, RoomInfoDto> {

    private HotelReadMapper hotelReadMapper;

    @Override
    public RoomInfoDto map(Room from) {
        return RoomInfoDto
                .builder()
                .price(from.getPrice())
                .roomClass(from.getRoomClass())
                .hotelInfoDto(hotelReadMapper.map(from.getHotel()))
                .roomNumber(from.getRoomNumber())
                .build();
    }

    @Autowired
    public void setHotelReadMapper(HotelReadMapper hotelReadMapper) {
        this.hotelReadMapper = hotelReadMapper;
    }
}