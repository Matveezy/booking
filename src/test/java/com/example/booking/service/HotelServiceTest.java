package com.example.booking.service;

import com.example.booking.dto.HotelInfoDto;
import com.example.booking.dto.HotelsFilterSearchDto;
import com.example.booking.entity.HotelClass;
import com.example.booking.exception.HotelNotFoundException;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@IT
@RequiredArgsConstructor
@Sql(value = {"/db/data/init-data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class HotelServiceTest extends IntegrationTestBase {

    private final HotelService hotelService;

    @Test
    void findHotelInfo() {
        var expected = HotelInfoDto.builder()
                .id(10000L)
                .city("London")
                .name("hotel1")
                .hotelClass(HotelClass.FIVE_STARS)
                .build();
        var actual = hotelService.findHotelInfo(10000);

        assertEquals(expected, actual);

        assertThrows(HotelNotFoundException.class, () -> hotelService.findHotelInfo(-10));
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

}