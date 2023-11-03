package com.example.booking.service;

import com.example.booking.dto.RoomsFilterSearchDto;
import com.example.booking.entity.RoomClass;
import com.example.booking.repository.HotelRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IT
@RequiredArgsConstructor
@Sql("classpath:sql/data.sql")
public class RoomServiceTestIT extends IntegrationTestBase {

    private final RoomService roomService;
    private final HotelRepository hotelRepository;

    private Instant parseDate(String date) {
        return LocalDate.parse(date).atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
    }

    @Test
    public void roomTest() {
        var hotel = hotelRepository.findHotelByName("Novotel").get().getId();
        var filter = RoomsFilterSearchDto.builder().build();
        var rooms = roomService.findRoomsByDates(filter, hotel, 0);
        assertEquals(rooms.size(), 4);

        filter = filter.withRoomClass(RoomClass.DOUBLE);

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        assertEquals(rooms.size(), 1);
        assertEquals(rooms.get(0).getPrice(), 3200);

        filter = filter
                .withDateIn(parseDate("2023-12-30"))
                .withDateOut(parseDate("2024-01-07"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        assertEquals(rooms.size(), 0);

        filter = filter
                .withDateIn(parseDate("2023-12-30"))
                .withDateOut(parseDate("2024-01-05"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        assertEquals(rooms.size(), 0);

        filter = filter
                .withDateIn(parseDate("2023-11-06"))
                .withDateOut(parseDate("2023-11-07"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        assertEquals(rooms.size(), 1);

        filter = filter
                .withDateIn(parseDate("2023-11-07"))
                .withDateOut(parseDate("2024-01-06"));

        rooms = roomService.findRoomsByDates(filter, hotel, 0);
        assertEquals(rooms.size(), 0);
    }
}
