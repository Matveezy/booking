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

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class HotelServiceTest extends IntegrationTestBase {

    private final HotelService hotelService;

    private final String OWNER_LOGIN = "alexandr@gmail.com";

    private final long NOT_EXIST_HOTEL_ID = 1488;

    private final long EXIST_HOTEL_ID = 3;

    private final long OTHER_HOTEL_ID = 1;


    @Test
    void findHotelInfo() {
        var expected = HotelInfoDto.builder()
                .id(1L)
                .city("Saint-Petersburg")
                .name("Domina St. Petersburg")
                .hotelClass(HotelClass.THREE_STARS)
                .build();
        var actual = hotelService.findHotelInfo(1L);

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
                .isEqualTo(7);
        assertThat(page1)
                .containsAll(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
    }

    @Test
    void findHotelsByCityTest() {
        var hotels1 = hotelService.findHotelsByCity("Saint-Petersburg", 0);
        assertThat(hotels1.size())
                .isEqualTo(6);

        var hotels2 = hotelService.findHotelsByCity("Amsterdam", 0);
        assertThat(hotels2.size())
                .isEqualTo(1);
        assertThat(hotels2.get(0).getName())
                .isEqualTo("Kempinski Hotel Moika 22");
    }

    @Test
    void findHotelsByCityAndClassTest() {
        var hotels = hotelService.findHotelsByCityAndHotelClass("Saint-Petersburg", HotelClass.FIVE_STARS, 0);
        assertThat(hotels.size())
                .isEqualTo(2);
        assertThat(hotels.get(0).getName())
                .isEqualTo("Taleon Imperial");
    }

    @Test
    void findHotelsByFilter() {
        var filter = new HotelsFilterSearchDto();
        assertThat(hotelService.findHotels(filter, 0).size())
                .isEqualTo(7);
        filter = filter.withHotelClass(HotelClass.FIVE_STARS);
        filter.setCity("Saint-Petersburg");
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