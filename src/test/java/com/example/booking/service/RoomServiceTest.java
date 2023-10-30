package com.example.booking.service;

import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


@IT
@RequiredArgsConstructor
class RoomServiceTest extends IntegrationTestBase {

    private final RoomService roomService;

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
}