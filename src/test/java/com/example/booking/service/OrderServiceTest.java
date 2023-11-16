package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import com.example.booking.dto.OrderReadDto;
import com.example.booking.dto.RoomInfoDto;
import com.example.booking.repository.UserRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@IT
@RequiredArgsConstructor
public class OrderServiceTest extends IntegrationTestBase {

    private final OrdersService ordersService;
    private final UserService userService;
    private final RoomService roomService;
    private final HotelService hotelService;

    @Test
    public void makeOrderTest() {
        var userOpt = userService.findUserByName("ivan@gmail.com");
        assertThat(userOpt).isPresent();

        var room = roomService.findRoom(1);
        assertThat(room).isNotNull();

        var userId = userOpt.get().getId();
        var order = BookRoomDto.builder()
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(20000))
                .userId(userId)
                .build();
        ordersService.makeOrder(order, room.getId());

        assertThat(ordersService.showOrders(userId))
                .contains(OrderReadDto.builder()
                        .dateIn(Instant.ofEpochSecond(10000))
                        .dateOut(Instant.ofEpochSecond(20000))
                        .room(roomService.findRoomInfo(room.getId()).orElse(null))
                        .hotel(hotelService.findHotelInfo(room.getHotel().getId()).orElse(null))
                        .userReadDto(userService.findUserById(userId).orElse(null))
                        .build()
                );
    }
}
