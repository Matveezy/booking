package com.example.booking.service;

import com.example.booking.entity.*;
import com.example.booking.exception.RoomNotFoundException;
import com.example.booking.mapper.RoomReadMapper;
import com.example.booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;

import com.example.booking.dto.*;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.repository.HotelRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    public final static int MAX_PAGE_SIZE = 10;

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomReadMapper roomReadMapper;

    @Transactional
    public Long saveRoom(RoomCreateDto roomCreateDto) throws AccessDeniedException {
        Optional<Hotel> optionalHotel = hotelRepository.findHotelById(roomCreateDto.getHotelId());
        if (optionalHotel.isEmpty()) {
            throw new HotelNotFoundException(roomCreateDto.getHotelId());
        }
        Hotel hotel = optionalHotel.get();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
            Room room = Room.builder()
                    .hotel(hotel)
                    .roomClass(roomCreateDto.getRoomClass())
                    .roomNumber(roomCreateDto.getRoomNumber())
                    .price(roomCreateDto.getPrice())
                    .build();

            roomRepository.save(room);
            return room.getId();
        } else {
            throw new AccessDeniedException("You do not have permission to create a room in this hotel");
        }
    }

    @Transactional
    public void updateRoom(RoomUpdateDto roomUpdateDto, long roomId) throws AccessDeniedException {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        Room room = optionalRoom.get();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Hotel hotel = hotelRepository.findHotelById(roomRepository.findRoomById(roomId).get().getHotel().getId()).get();

        if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
            room.setRoomClass(roomUpdateDto.getRoomClass());
            room.setRoomNumber(roomUpdateDto.getRoomNumber());
            room.setPrice(roomUpdateDto.getPrice());

            roomRepository.save(room);
        } else {
            throw new AccessDeniedException("You do not have permission to modify this room");
        }
    }

    @Transactional
    public void deleteRoom(long roomId) throws AccessDeniedException {
        if (roomRepository.existsById(roomId)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Hotel hotel = hotelRepository.findHotelById(roomRepository.findRoomById(roomId).get().getHotel().getId()).get();
            if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
                roomRepository.deleteById(roomId);
            } else {
                throw new AccessDeniedException("You do not have permission to delete this room");
            }
        } else {
            throw new RoomNotFoundException(roomId);
        }
    }

    public Room findRoom(long id) {
        return roomRepository.findRoomById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
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
                .map(roomReadMapper::map)
                .toList();
    }
}
