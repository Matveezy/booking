package booking.service;

import booking.dto.RoomCreateDto;
import booking.dto.RoomInfoDto;
import booking.dto.RoomUpdateDto;
import booking.dto.RoomsFilterSearchDto;
import booking.entity.Hotel;
import booking.entity.Room;
import booking.entity.RoomClass;
import booking.exception.NotEnoughPermissionException;
import booking.exception.RoomNotFoundException;
import booking.mapper.RoomEntityMapper;
import booking.mapper.RoomReadMapper;
import booking.entity.*;
import booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import booking.dto.*;
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
    private final RoomReadMapper roomReadMapper;
    private final OrdersService ordersService;
    private final RoomEntityMapper roomEntityMapper;

    @Transactional
    public Long saveRoom(RoomCreateDto roomCreateDto) {
        Room roomEntityToSave = roomEntityMapper.map(roomCreateDto);
        Hotel hotel = roomEntityToSave.getHotel();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
            roomRepository.save(roomEntityToSave);
            return roomEntityToSave.getId();
        } else {
            throw new NotEnoughPermissionException("You do not have permission to create a room in this hotel");
        }
    }

    @Transactional
    public void updateRoom(RoomUpdateDto roomUpdateDto, long roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        Room room = optionalRoom.get();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Hotel hotel = room.getHotel();
        if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
            room.setRoomClass(roomUpdateDto.getRoomClass());
            room.setRoomNumber(roomUpdateDto.getRoomNumber());
            room.setPrice(roomUpdateDto.getPrice());

            roomRepository.save(room);
        } else {
            throw new NotEnoughPermissionException("You do not have permission to modify this room");
        }
    }

    @Transactional
    public boolean deleteRoom(long roomId) {
        if (roomRepository.existsById(roomId)) {
            Room room = roomRepository.findRoomById(roomId).get();
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Hotel hotel = room.getHotel();
            if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
                roomRepository.deleteById(roomId);
                return true;
            } else {
                throw new NotEnoughPermissionException("You do not have permission to delete this room");
            }
        }
        return false;
    }

    public Optional<RoomInfoDto> findRoom(long id) {
        return roomRepository.findRoomById(id)
                .map(roomReadMapper::map);
    }

    private List<RoomInfoDto> findRoomsByHotel(long hotel, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return roomRepository.findRoomsByHotel_Id(hotel, pageable)
                .stream().map(roomReadMapper::map)
                .toList();
    }

    private List<RoomInfoDto> findRoomsByHotelAndRoomClass(long hotel, RoomClass roomClass, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return roomRepository.findRoomsByRoomClassAndHotel_Id(roomClass, hotel, pageable)
                .stream().map(roomReadMapper::map).toList();
    }

    private List<RoomInfoDto> findRooms(RoomsFilterSearchDto filter, long hotel, int page) {
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

    public boolean isRoomFree(RoomInfoDto room, Instant dateIn, Instant dateOut) {
        return ordersService.findOrdersByRoomId(room.getId()).stream()
                .filter(o -> datesIntersection(o.getDateIn(), o.getDateOut(), dateIn, dateOut))
                .toList()
                .isEmpty();
    }

    public List<RoomInfoDto> findRoomsByDates(RoomsFilterSearchDto filter, long hotel, int page) {
        var in = filter.getDateIn() == null ? Instant.MIN : filter.getDateIn();
        var out = filter.getDateOut() == null ? Instant.MIN : filter.getDateOut();
        return findRooms(filter, hotel, page).stream()
                .filter(r -> isRoomFree(r, in, out))
                .toList();
    }
}
