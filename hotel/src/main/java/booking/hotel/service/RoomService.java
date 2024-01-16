package booking.hotel.service;

import booking.hotel.dto.RoomCreateDto;
import booking.hotel.dto.RoomReadDto;
import booking.hotel.dto.RoomUpdateDto;
import booking.hotel.dto.RoomsFilterSearchDto;
import booking.hotel.entity.Hotel;
import booking.hotel.entity.Room;
import booking.hotel.entity.RoomClass;
import booking.hotel.exception.NotEnoughPermissionException;
import booking.hotel.mapper.RoomCreateMapper;
import booking.hotel.mapper.RoomReadMapper;
import booking.hotel.repo.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    public final static int MAX_PAGE_SIZE = 10;

    private final RoomRepository roomRepository;
    private final RoomReadMapper roomReadMapper;
    private final HotelService hotelService;
    private final RoomCreateMapper roomCreateMapper;
    private final OrderService orderService;

    @Transactional
    public RoomReadDto createRoom(Long userId, RoomCreateDto roomCreateDto) {
        Optional<Hotel> hotelById = hotelService.findHotelById(roomCreateDto.getHotelId());
        return hotelById.map(hotel -> {
                    if (!hotel.getUserId().contains(userId))
                        throw new NotEnoughPermissionException("You have no permission to create room in this hotel");
                    Room roomToCreate = roomCreateMapper.mapToEntity(roomCreateDto);
                    roomToCreate.setHotel(hotel);
                    roomRepository.save(roomToCreate);
                    return roomReadMapper.mapToDto(roomToCreate);
                }
        ).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    public RoomReadDto updateRoom(Long userId, RoomUpdateDto roomUpdateDto) {
        Optional<Room> roomById = roomRepository.findRoomById(roomUpdateDto.getId());
        if (roomById.isEmpty()) throw new EntityNotFoundException();
        Room roomEntity = roomById.get();
        Optional<Hotel> hotelById = hotelService.findHotelById(roomEntity.getHotel().getId());
        return hotelById.map(hotel -> {
            if (!hotel.getUserId().contains(userId))
                throw new NotEnoughPermissionException("You have no permission to update room in this hotel");

            if (roomUpdateDto.getRoomClass() != null) roomEntity.setRoomClass(roomUpdateDto.getRoomClass());
            if (roomUpdateDto.getRoomNumber() != null) roomEntity.setRoomNumber(roomUpdateDto.getRoomNumber());
            if (roomUpdateDto.getPrice() != null) roomEntity.setPrice(roomUpdateDto.getPrice());
            roomRepository.save(roomEntity);
            return roomReadMapper.mapToDto(roomEntity);
        }).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    public boolean deleteRoom(Long userId, Long roomId) {
        Optional<Room> roomById = roomRepository.findRoomById(roomId);
        if (roomById.isEmpty()) throw new EntityNotFoundException();
        Room roomEntity = roomById.get();
        Optional<Hotel> hotelById = hotelService.findHotelById(roomEntity.getHotel().getId());
        return hotelById.map(hotel -> {
            if (!hotel.getUserId().contains(userId))
                throw new NotEnoughPermissionException("You have no permission to delete room in this hotel");
            roomRepository.delete(roomEntity);
            return true;
        }).orElseThrow(() -> new EntityNotFoundException());
    }

    public Optional<Room> findRoomById(long roomId){
        return roomRepository.findRoomById(roomId);
    }

    public List<RoomReadDto> findRoomsByHotel(long hotel, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return roomRepository.findRoomsByHotel_Id(hotel, pageable)
                .stream().map(roomReadMapper::mapToDto)
                .toList();
    }

    private List<RoomReadDto> findRoomsByHotelAndRoomClass(long hotel, RoomClass roomClass, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return roomRepository.findRoomsByRoomClassAndHotel_Id(roomClass, hotel, pageable)
                .stream().map(roomReadMapper::mapToDto).toList();
    }

    private List<RoomReadDto> findRooms(RoomsFilterSearchDto filter, long hotel, int page) {
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

    public boolean isRoomFree(Long roomId, Instant dateIn, Instant dateOut) {
        return orderService.getOrdersByRoomId(roomId).stream()
                .filter(o -> datesIntersection(o.getDateIn(), o.getDateOut(), dateIn, dateOut))
                .toList()
                .isEmpty();
    }

    public List<RoomReadDto> findRoomsByDates(RoomsFilterSearchDto filter, long hotel, int page) {
        var in = filter.getDateIn() == null ? Instant.MIN : filter.getDateIn();
        var out = filter.getDateOut() == null ? Instant.MIN : filter.getDateOut();
        return findRooms(filter, hotel, page).stream()
                .filter(r -> isRoomFree(r.getId(), in, out))
                .toList();
    }
}
