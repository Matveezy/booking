package com.example.booking.mapper;

import com.example.booking.dto.HotelInfoDto;
import com.example.booking.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelReadMapper implements Mapper<Hotel, HotelInfoDto> {

    @Override
    public HotelInfoDto map(Hotel from) {
        return HotelInfoDto.builder()
                .id(from.getId())
                .name(from.getName())
                .hotelClass(from.getHotelClass())
                .city(from.getCity())
                .build();
    }
}