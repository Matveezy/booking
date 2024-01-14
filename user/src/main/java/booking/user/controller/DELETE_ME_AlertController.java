package booking.user.controller;

import booking.user.dto.OrderInfoDto;
import booking.user.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DELETE_ME_AlertController {

    private final OrdersService ordersService;

    @PostMapping("/alert")
    public void alertOrder(@RequestBody OrderInfoDto orderInfoDto) {
        ordersService.alertOrderCreated(orderInfoDto);
    }

}
