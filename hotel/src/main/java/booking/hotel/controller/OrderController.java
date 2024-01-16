package booking.hotel.controller;

import booking.hotel.dto.OrderCreateDto;
import booking.hotel.dto.OrderInfoDto;
import booking.hotel.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    @PreAuthorize("hasAnyAuthority('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<OrderInfoDto> makeOrder(HttpServletRequest httpServletRequest, @RequestBody @Validated OrderCreateDto orderCreateDto) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(orderService.makeOrder(Long.valueOf(userId), orderCreateDto), HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<OrderInfoDto>> getOrdersByRoomId(@RequestParam long roomId) {
        return ResponseEntity.ok(orderService.getOrdersByRoomId(roomId));
    }
}
