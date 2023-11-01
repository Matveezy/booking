package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import com.example.booking.dto.OrderDto;
import com.example.booking.entity.Order;
import com.example.booking.repository.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class OrdersService {

    private OrdersRepository ordersRepository;
    private RoomService roomService;
    private UserService userService;

    public void makeOrder(BookRoomDto orderDto, long roomId) {
        var room = roomService.findRoom(roomId);
        var user = userService.findUserById(orderDto.getUserId());
        if (roomService.isRoomFree(room, orderDto.getDateIn(), orderDto.getDateOut())) {
            var order = Order.builder()
                    .createdAt(Instant.now())
                    .dateIn(orderDto.getDateIn())
                    .dateOut(orderDto.getDateOut())
                    .user(user)
                    .hotel(room.getHotel())
                    .build();
            ordersRepository.save(order);
        }
    }

    public List<OrderDto> showOrders(long userId) {
        return ordersRepository.findOrdersByUser_Id(userId).stream()
                .map(OrderDto::new)
                .toList();
    }

    public void cancelOrder(long orderId) {
        ordersRepository.deleteById(orderId);
    }
}
