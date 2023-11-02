package com.example.booking.mapper;

import com.example.booking.dto.OrderReadDto;
import com.example.booking.entity.Order;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    private final UserReadMapper userReadMapper;
    private final HotelReadMapper hotelReadMapper;

    @Autowired
    private RoomReadMapper roomReadMapper;

    @PostConstruct
    public void init() {
        roomReadMapper.setOrderReadMapper(this);
    }

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