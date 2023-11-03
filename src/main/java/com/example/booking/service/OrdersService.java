package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import com.example.booking.dto.OrderReadDto;
import com.example.booking.dto.UserReadDto;
import com.example.booking.entity.Order;
import com.example.booking.mapper.OrderReadMapper;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.OrdersRepository;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final RoomService roomService;
    private final UserService userService;
    private final OrderReadMapper orderReadMapper;
    private final UserRepository userRepository;

    @Transactional
    public void makeOrder(BookRoomDto orderDto, long roomId) {
        var room = roomService.findRoom(roomId);
        var user = userRepository.findUserById(orderDto.getUserId());
        if (roomService.isRoomFree(room, orderDto.getDateIn(), orderDto.getDateOut())) {
            var order = Order.builder()
                    .createdAt(Instant.now())
                    .dateIn(orderDto.getDateIn())
                    .dateOut(orderDto.getDateOut())
                    .user(user.get())
                    .hotel(room.getHotel())
                    .build();
            ordersRepository.save(order);
        }
    }

    public List<OrderReadDto> showOrders(long userId) {
        return ordersRepository.findOrdersByUser_Id(userId).stream()
                .map(orderReadMapper::map)
                .toList();
    }

    @Transactional
    public void cancelOrder(long orderId) {
        ordersRepository.deleteById(orderId);
    }
}
