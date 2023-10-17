package com.example.booking.service;

import com.example.booking.dto.RoomCreateDto;
import com.example.booking.dto.RoomUpdateDto;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.Room;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.exception.RoomNotFoundException;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public Long saveRoom(RoomCreateDto roomCreateDto) {
        Optional<Hotel> optionalHotel = hotelRepository.findHotelById(roomCreateDto.getHotelId());
        if (optionalHotel.isEmpty()) {
            throw new HotelNotFoundException(roomCreateDto.getHotelId());
        }
        Hotel hotel = optionalHotel.get();

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomClass(roomCreateDto.getRoomClass());
        room.setRoomNumber(roomCreateDto.getRoomNumber());
        room.setPrice(roomCreateDto.getPrice());

        Room savedRoom = roomRepository.save(room);
        return savedRoom.getId();
    }

    public void updateRoom(RoomUpdateDto roomUpdateDto, long roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        Room room = optionalRoom.get();

        room.setRoomClass(roomUpdateDto.getRoomClass());
        room.setRoomNumber(roomUpdateDto.getRoomNumber());
        room.setPrice(roomUpdateDto.getPrice());

        roomRepository.save(room);
    }

    public void deleteRoom(long roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
        } else {
            throw new RoomNotFoundException(roomId);
        }
    }
}
