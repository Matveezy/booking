package booking.mapper;

import booking.dto.OrderReadDto;
import booking.entity.Order;
import booking.repository.OrdersRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class OrderReadMapperTest extends IntegrationTestBase {
    private final OrderReadMapper mapper;
    private final OrdersRepository repository;

    private final long EXISTS_ORDER_ID = 1;
    @Test
    void map() {
        Order order = repository.findById(EXISTS_ORDER_ID).get();
        OrderReadDto dto = mapper.map(order);
        assertThat(dto.in()).isEqualTo(order.getDateIn());
    }
}
