package com.example.booking.service;

import com.example.booking.dto.HotelCreateDto;
import com.example.booking.dto.HotelInfoDto;
import com.example.booking.dto.HotelUpdateDto;
import com.example.booking.dto.HotelsFilterSearchDto;
import com.example.booking.entity.HotelClass;
import com.example.booking.exception.HotelNotFoundException;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import integration.annotation.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@IT
@RequiredArgsConstructor
class HotelServiceTest extends IntegrationTestBase {

    private final HotelService hotelService;

    private final String OWNER_LOGIN = "alexandr@gmail.com";

    private final long NOT_EXIST_HOTEL_ID = 1488;

    private final long EXIST_HOTEL_ID = 13;

    private final long OTHER_HOTEL_ID = 11;


    @Test
    void findHotelInfo() {
        var expected = HotelInfoDto.builder()
                .id(10000L)
                .city("London")
                .name("hotel1")
                .hotelClass(HotelClass.FIVE_STARS)
                .build();
        var actual = hotelService.findHotelInfo(10000);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void findHotelsPaginationTest() {
        var page1 = hotelService.findHotels(0).stream()
                .map(HotelInfoDto::getId)
                .toList();
        var page2 = hotelService.findHotels(1).stream()
                .map(HotelInfoDto::getId)
                .toList();
        var page3 = hotelService.findHotels(2).stream()
                .map(HotelInfoDto::getId)
                .toList();

        assertThat(page1.size())
                .isEqualTo(10);
        assertThat(page1)
                .containsAll(List.of(10000L, 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L));

        assertThat(page2.size())
                .isEqualTo(10);
        assertThat(page2)
                .containsAll(List.of(1000L, 1100L, 1200L, 1300L, 1400L, 1500L, 1600L, 1700L, 1800L, 1900L));

        assertThat(page3.size())
                .isEqualTo(1);
        assertThat(page3)
                .containsAll(List.of(2000L));
    }

    @Test
    void findHotelsByCityTest() {
        var hotels1 = hotelService.findHotelsByCity("Paris", 0);
        assertThat(hotels1.size())
                .isEqualTo(6);

        var hotels2 = hotelService.findHotelsByCity("Amsterdam", 0);
        assertThat(hotels2.size())
                .isEqualTo(1);
        assertThat(hotels2.get(0).getName())
                .isEqualTo("hotel10");
    }

    @Test
    void findHotelsByCityAndClassTest() {
        var hotels = hotelService.findHotelsByCityAndHotelClass("London", HotelClass.FIVE_STARS, 0);
        assertThat(hotels.size())
                .isEqualTo(1);
        assertThat(hotels.get(0).getName())
                .isEqualTo("hotel1");
    }

    @Test
    void findHotelsByFilter() {
        var filter = new HotelsFilterSearchDto();
        assertThat(hotelService.findHotels(filter, 0).size())
                .isEqualTo(10);
        filter = filter.withCity("Paris");
        assertThat(hotelService.findHotels(filter, 0).size())
                .isEqualTo(6);
        filter = filter.withHotelClass(HotelClass.FIVE_STARS);
        assertThat(hotelService.findHotels(filter, 0).size())
                .isEqualTo(2);

    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void deleteHotelSuccess() {
        assertDoesNotThrow(() -> hotelService.deleteHotel(EXIST_HOTEL_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void deleteHotelNotExists() {
        assertThrows(HotelNotFoundException.class,
                () -> hotelService.deleteHotel(NOT_EXIST_HOTEL_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void deleteHotelAccessDenied() {
        assertThrows(AccessDeniedException.class,
                () -> hotelService.deleteHotel(OTHER_HOTEL_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateHotelSuccess() {
        HotelUpdateDto hotelUpdateDto = HotelUpdateDto.builder()
                .name("asdasd")
                .hotelClass(HotelClass.FIVE_STARS)
                .city("qweqwe")
                .build();
        assertDoesNotThrow(() -> {
            hotelService.updateHotel(hotelUpdateDto, EXIST_HOTEL_ID);
        });
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateHotelNotExists() {
        HotelUpdateDto hotelUpdateDto = HotelUpdateDto.builder()
                .name("asdasd")
                .hotelClass(HotelClass.FIVE_STARS)
                .city("qweqwe")
                .build();
        assertThrows(HotelNotFoundException.class,
                () -> hotelService.updateHotel(hotelUpdateDto, NOT_EXIST_HOTEL_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateHotelAccessDenied() {
        HotelUpdateDto hotelUpdateDto = HotelUpdateDto.builder()
                .name("asdasd")
                .hotelClass(HotelClass.FIVE_STARS)
                .city("qweqwe")
                .build();
        assertThrows(AccessDeniedException.class,
                () -> hotelService.updateHotel(hotelUpdateDto, OTHER_HOTEL_ID));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void saveHotelSuccess() {
        HotelCreateDto hotelCreateDto = HotelCreateDto.builder()
                .name("asdasd")
                .hotelClass(HotelClass.FIVE_STARS)
                .city("qweqwe")
                .build();

        assertDoesNotThrow(() -> {
            hotelService.saveHotel(hotelCreateDto);
        });
    }

}