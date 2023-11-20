package com.example.booking.mapper;

import com.example.booking.dto.CreateOrderDto;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.Order;
import com.example.booking.entity.Room;
import com.example.booking.entity.User;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderEntityMapper implements Mapper<CreateOrderDto, Order> {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public Order map(CreateOrderDto createOrderDto) {
        Optional<User> maybeUser = userRepository.findUserById(createOrderDto.getUserId());
        if (maybeUser.isEmpty())
            throw new EntityNotFoundException("User with id: " + createOrderDto.getUserId() + " not found!");
        Optional<Room> maybeRoom = roomRepository.findRoomById(createOrderDto.getRoomId());
        if (maybeRoom.isEmpty())
            throw new EntityNotFoundException("Room with id + " + createOrderDto.getRoomId() + "not found!");
        return Order.builder()
                .user(maybeUser.get())
                .hotel(maybeRoom.get().getHotel())
                .room(maybeRoom.get())
                .dateIn(createOrderDto.getDateIn())
                .dateOut(createOrderDto.getDateOut())
                .createdAt(Instant.now())
                .build();
    }
}
