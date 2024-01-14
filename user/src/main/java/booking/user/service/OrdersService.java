package booking.user.service;

import booking.user.dto.OrderInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final KafkaTemplate<Long, OrderInfoDto> kafkaTemplate;

    public void alertOrderCreated(OrderInfoDto order) {
        kafkaTemplate.send("alerts", order);
    }

}
