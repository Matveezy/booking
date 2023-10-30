package com.example.booking.service;

import com.example.booking.entity.*;
import com.example.booking.exception.RoomNotFoundException;
import com.example.booking.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import com.example.booking.dto.*;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.repository.HotelRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
    public final static int MAX_PAGE_SIZE = 10;

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

    public RoomInfoDto findRoomInfo(long id) {
        var room = roomRepository.findRoomById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        return new RoomInfoDto(room);
    }

    private List<Room> findRoomsByHotel(long hotel, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return roomRepository.findRoomsByHotel_Id(hotel, pageable);
    }

    private List<Room> findRoomsByHotelAndRoomClass(long hotel, RoomClass roomClass, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return roomRepository.findRoomsByRoomClassAndHotel_Id(roomClass, hotel, pageable);
    }

    private List<Room> findRooms(RoomsFilterSearchDto filter, long hotel, int page) {
        if (filter.getRoomClass() == null) {
            return findRoomsByHotel(hotel, page);
        } else {
            return findRoomsByHotelAndRoomClass(hotel, filter.getRoomClass(), page);
        }
    }

    public boolean datesIntersection(Instant dateIn1, Instant dateOut1, Instant dateIn2, Instant dateOut2) {
        var maxStart = dateIn1.isAfter(dateIn2) ? dateIn1 : dateIn2;
        var minEnd = dateOut1.isBefore(dateOut2) ? dateOut1 : dateOut2;
        return maxStart.isBefore(minEnd) || maxStart.equals(minEnd);
    }

    public boolean isRoomFree(Room room, Instant dateIn, Instant dateOut) {
        return room.getOrdersThisRoom().stream()
                .filter(o -> datesIntersection(o.getDateIn(), o.getDateOut(), dateIn, dateOut))
                .toList()
                .isEmpty();
    }

    public List<RoomInfoDto> findRoomsByDates(RoomsFilterSearchDto filter, long hotel, int page) {
        var in = filter.getDateIn() == null ? Instant.MIN : filter.getDateIn();
        var out = filter.getDateOut() == null ? Instant.MIN : filter.getDateOut();
        return findRooms(filter, hotel, page).stream()
                .filter(r -> isRoomFree(r, in, out))
                .map(RoomInfoDto::new)
                .toList();
    }

}
