package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@IT
@RequiredArgsConstructor
public class OrderServiceTest extends IntegrationTestBase {

    private final OrdersService ordersService;

    //todo нормальные тесты
    @Test
    public void makeOrderTest() {
        var order = BookRoomDto.builder()
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(20000))
                .userId(100)
                .build();
        ordersService.makeOrder(order, 100);
    }
}
