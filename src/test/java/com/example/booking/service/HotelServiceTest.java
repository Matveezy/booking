package com.example.booking.service;

import com.example.booking.dto.HotelInfoDto;
import com.example.booking.dto.HotelsFilterSearchDto;
import com.example.booking.entity.HotelClass;
import com.example.booking.exception.HotelNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@Sql(value = "/db/data/hotel-data.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(value = "/db/data/clear-data.sql", executionPhase = AFTER_TEST_METHOD)
class HotelServiceTest {

    @Autowired
    private HotelService hotelService;

    @Test
    void findHotelInfo() {
        var expected = HotelInfoDto.builder()
                .id(0L)
                .city("London")
                .name("hotel1")
                .userScore(90L)
                .hotelClass(HotelClass.FIVE_STARS)
                .build();
        var actual = hotelService.findHotelInfo(0);

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
                .containsAll(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));

        assertThat(page2.size())
                .isEqualTo(10);
        assertThat(page2)
                .containsAll(List.of(10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L));

        assertThat(page3.size())
                .isEqualTo(1);
        assertThat(page3)
                .containsAll(List.of(20L));
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