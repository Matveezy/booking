package com.example.booking.mapper;

import com.example.booking.dto.CreateOrderDto;
import com.example.booking.entity.Order;
import com.example.booking.exception.EntityNotFoundException;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class OrderEntityMapperTest extends IntegrationTestBase {
    private final OrderEntityMapper mapper;

    private final long EXISTS_USER_ID = 1;
    private final long EXISTS_ROOM_ID = 1;
    private final long NOT_EXISTS_USER_ID = 228;
    private final Instant DATE_IN = Instant.MIN;

    @Test
    void mapEntityNotExist() {
        CreateOrderDto dto = CreateOrderDto.builder()
                .userId(NOT_EXISTS_USER_ID)
                .roomId(EXISTS_ROOM_ID)
                .build();
        Assertions.assertThrows(EntityNotFoundException.class,() -> mapper.map(dto));
    }

    @Test
    void mapSuccess() {
        CreateOrderDto dto = CreateOrderDto.builder()
                .userId(EXISTS_USER_ID)
                .dateIn(DATE_IN)
                .roomId(EXISTS_ROOM_ID)
                .build();
        Order order = mapper.map(dto);
        assertThat(order.getDateIn()).isEqualTo(DATE_IN);
    }
}
