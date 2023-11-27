package com.example.booking.mapper;

import com.example.booking.dto.RoomCreateDto;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.Room;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.repository.HotelRepository;
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
