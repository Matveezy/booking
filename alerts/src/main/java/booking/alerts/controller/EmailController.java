package booking.alerts.controller;

import booking.alerts.dto.OrderInfoDto;
import booking.alerts.service.BookingEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final BookingEmailService bookingEmailService;

    @PostMapping("/alert")
    public void alertOrder(@RequestBody OrderInfoDto orderInfoDto) {
        bookingEmailService.sendOrderEmail(orderInfoDto);
    }

    @KafkaListener(topics = "alerts", groupId = "alerts-group", containerFactory = "singleFactory")
    public void alertOrderKafka(@Payload OrderInfoDto orderInfoDto) {
        bookingEmailService.sendOrderEmail(orderInfoDto);
    }
}
