package com.example.booking.service;

import com.example.booking.dto.RoomCreateDto;
import com.example.booking.dto.RoomUpdateDto;
import com.example.booking.entity.RoomClass;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.exception.RoomNotFoundException;
import com.example.booking.dto.RoomsFilterSearchDto;
import com.example.booking.entity.RoomClass;
import com.example.booking.repository.HotelRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import integration.annotation.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@IT
@RequiredArgsConstructor
@Sql("classpath:sql/data.sql")
class RoomServiceTest extends IntegrationTestBase {

    private final RoomService roomService;
    private final HotelRepository hotelRepository;

    private final String OWNER_LOGIN = "alexandr@gmail.com";

    private final long NOT_EXIST_HOTEL_ID = 1488;

    private final long EXIST_HOTEL_ID = 13;

    private final long OTHER_HOTEL_ID = 11;

    private final long EXIST_ROOM_ID = 11;

    private final long NOT_EXIST_ROOM_ID = 1337;

    private final long OTHER_ROOM_ID = 110;

    private static Stream<Arguments> datesSource() {
        return Stream.of(
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), Instant.ofEpochSecond(30), Instant.ofEpochSecond(40), false),
                Arguments.of(Instant.ofEpochSecond(30), Instant.ofEpochSecond(40), Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), false),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), Instant.ofEpochSecond(15), Instant.ofEpochSecond(40), true),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(35), Instant.ofEpochSecond(30), Instant.ofEpochSecond(40), true),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), Instant.ofEpochSecond(12), Instant.ofEpochSecond(15), true),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), true),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), Instant.ofEpochSecond(20), Instant.ofEpochSecond(30), true),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), Instant.ofEpochSecond(10), Instant.ofEpochSecond(20), true),
                Arguments.of(Instant.ofEpochSecond(10), Instant.ofEpochSecond(10), Instant.ofEpochSecond(10), Instant.ofEpochSecond(10), true)
        );
    }

    @ParameterizedTest
    @MethodSource("datesSource")
    void datesIntersectionTest(Instant dateIn1, Instant dateOut1, Instant dateIn2, Instant dateOut2, boolean expected) {
        assertEquals(expected, roomService.datesIntersection(dateIn1, dateOut1, dateIn2, dateOut2));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void deleteRoomSuccess() {
        assertDoesNotThrow(() -> roomService.deleteRoom(EXIST_ROOM_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void deleteRoomNotExists() {
        assertThrows(RoomNotFoundException.class,
                () -> roomService.deleteRoom(NOT_EXIST_ROOM_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void deleteHotelAccessDenied() {
        assertThrows(AccessDeniedException.class,
                () -> roomService.deleteRoom(OTHER_ROOM_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateRoomSuccess() {
        RoomUpdateDto roomUpdateDto = RoomUpdateDto.builder()
                .roomClass(RoomClass.DOUBLE)
                .roomNumber(13)
                .price(10000)
                .build();
        assertDoesNotThrow(() -> roomService.updateRoom(roomUpdateDto, EXIST_ROOM_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateRoomNotExists() {
        RoomUpdateDto roomUpdateDto = RoomUpdateDto.builder()
                .roomClass(RoomClass.DOUBLE)
                .roomNumber(13)
                .price(10000)
                .build();
        assertThrows(RoomNotFoundException.class,
                () -> roomService.updateRoom(roomUpdateDto, NOT_EXIST_ROOM_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateRoomAccessDenied() {
        RoomUpdateDto roomUpdateDto = RoomUpdateDto.builder()
                .roomClass(RoomClass.DOUBLE)
                .roomNumber(13)
                .price(10000)
                .build();
        assertThrows(AccessDeniedException.class,
                () -> roomService.updateRoom(roomUpdateDto, OTHER_ROOM_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void saveRoomSuccess() {
        RoomCreateDto roomCreateDto = RoomCreateDto.builder()
                .hotelId(EXIST_HOTEL_ID)
                .roomClass(RoomClass.DOUBLE)
                .roomNumber(13)
                .price(10000)
                .build();
        assertDoesNotThrow(() -> roomService.saveRoom(roomCreateDto));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void saveRoomNotExists() {
        RoomCreateDto roomCreateDto = RoomCreateDto.builder()
                .hotelId(NOT_EXIST_HOTEL_ID)
                .roomClass(RoomClass.DOUBLE)
                .roomNumber(13)
                .price(10000)
                .build();
        assertThrows(HotelNotFoundException.class,
                () -> roomService.saveRoom(roomCreateDto));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateHotelAccessDenied() {
        RoomCreateDto roomCreateDto = RoomCreateDto.builder()
                .hotelId(OTHER_HOTEL_ID)
                .roomClass(RoomClass.DOUBLE)
                .roomNumber(13)
                .price(10000)
                .build();
        assertThrows(AccessDeniedException.class,
                () -> roomService.saveRoom(roomCreateDto));
    }


    private Instant parseDate(String date) {
        return LocalDate.parse(date).atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
    }

    @Test
    public void roomTest() {
        var hotel = hotelRepository.findHotelByName("Novotel").get().getId();
        var filter = RoomsFilterSearchDto.builder().build();
        var rooms = roomService.findRoomsByDates(filter, hotel, 0);
        Assertions.assertEquals(rooms.size(), 4);

        filter = filter.withRoomClass(RoomClass.DOUBLE);

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        Assertions.assertEquals(rooms.size(), 1);
        Assertions.assertEquals(rooms.get(0).getPrice(), 3200);

        filter = filter
                .withDateIn(parseDate("2023-12-30"))
                .withDateOut(parseDate("2024-01-07"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        Assertions.assertEquals(rooms.size(), 0);

        filter = filter
                .withDateIn(parseDate("2023-12-30"))
                .withDateOut(parseDate("2024-01-05"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        Assertions.assertEquals(rooms.size(), 0);

        filter = filter
                .withDateIn(parseDate("2023-11-06"))
                .withDateOut(parseDate("2023-11-07"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        Assertions.assertEquals(rooms.size(), 1);

        filter = filter
                .withDateIn(parseDate("2023-11-07"))
                .withDateOut(parseDate("2024-01-06"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        Assertions.assertEquals(rooms.size(), 0);
    }
}