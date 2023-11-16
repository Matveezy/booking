package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import com.example.booking.dto.CreateOrderDto;
import com.example.booking.dto.OrderReadDto;
import com.example.booking.dto.UserReadDto;
import com.example.booking.entity.Order;
import com.example.booking.entity.User;
import com.example.booking.exception.NotEnoughRightsException;
import com.example.booking.mapper.OrderEntityMapper;
import com.example.booking.mapper.OrderReadMapper;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.OrdersRepository;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {

    private final WalletService walletService;
    private final UserService userService;
    private final OrdersRepository ordersRepository;
    private final RoomService roomService;
    private final OrderReadMapper orderReadMapper;
    private final OrderEntityMapper orderEntityMapper;

    @Transactional
    public void makeOrder(BookRoomDto orderDto, long roomId) {
        var room = roomService.findRoom(roomId);
        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .roomId(roomId)
                .userId(orderDto.getUserId())
                .dateOut(orderDto.getDateOut())
                .dateIn(orderDto.getDateIn())
                .build();
        if (roomService.isRoomFree(room, orderDto.getDateIn(), orderDto.getDateOut())) {
            Order order = orderEntityMapper.map(createOrderDto);
            ordersRepository.save(order);
            walletService.bookRoom(order);
        }
    }


    public List<OrderReadDto> showOrders(long userId) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userService.findUserByName(principal.getUsername())
                .orElseThrow(NotEnoughRightsException::new);

        if (!user.getId().equals(userId)) {
            throw new NotEnoughRightsException();
        }

        return ordersRepository.findOrdersByUser_Id(userId).stream()
                .map(orderReadMapper::map)
                .toList();
    }

    @Transactional
    public void cancelOrder(long orderId) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var order = ordersRepository.findById(orderId)
                        .orElseThrow(() -> new IllegalArgumentException("Order " + orderId + " not found"));
        var user = userService.findUserByName(principal.getUsername())
                .orElseThrow(NotEnoughRightsException::new);
        if (!order.getUser().equals(user)) {
            throw new NotEnoughRightsException();
        }

        walletService.cancelRoom(order);
        ordersRepository.deleteById(orderId);
    }
}
