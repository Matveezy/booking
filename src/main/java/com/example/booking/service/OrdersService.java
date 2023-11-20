package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import com.example.booking.dto.CreateOrderDto;
import com.example.booking.dto.OrderReadDto;
import com.example.booking.entity.Order;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.exception.NotEnoughPermissionException;
import com.example.booking.exception.RoomIsNotFreeException;
import com.example.booking.mapper.OrderEntityMapper;
import com.example.booking.mapper.OrderReadMapper;
import com.example.booking.mapper.RoomReadMapper;
import com.example.booking.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    private final WalletService walletService;
    private final UserService userService;
    private final OrdersRepository ordersRepository;
    private final RoomService roomService;
    private final OrderReadMapper orderReadMapper;
    private final OrderEntityMapper orderEntityMapper;
    private final RoomReadMapper roomReadMapper;

    public OrdersService(WalletService walletService, UserService userService, OrdersRepository ordersRepository, @Lazy RoomService roomService, OrderReadMapper orderReadMapper, OrderEntityMapper orderEntityMapper, RoomReadMapper roomReadMapper) {
        this.walletService = walletService;
        this.userService = userService;
        this.ordersRepository = ordersRepository;
        this.roomService = roomService;
        this.orderReadMapper = orderReadMapper;
        this.orderEntityMapper = orderEntityMapper;
        this.roomReadMapper = roomReadMapper;
    }

    @Transactional
    public void makeOrder(CreateOrderDto orderDto) {
        Order orderEntityToSave = orderEntityMapper.map(orderDto);
        if (roomService.isRoomFree(roomReadMapper.map(orderEntityToSave.getRoom()), orderDto.getDateIn(), orderDto.getDateOut())) {
            ordersRepository.save(orderEntityToSave);
            walletService.bookRoom(orderEntityToSave);
        } else throw new RoomIsNotFreeException("Can't book room from " + orderDto.getDateIn() + " to " +
                                                orderDto.getDateOut() + ". Room is already booked.");
    }


    public List<OrderReadDto> showOrders(long userId) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userService.findByLogin(principal.getUsername())
                .orElseThrow(() -> new NotEnoughPermissionException("You have not enough permissions for this"));

        if (!user.getId().equals(userId)) {
            throw new NotEnoughPermissionException("You have not enough permissions for this");
        }

        return ordersRepository.findOrdersByUser_Id(userId).stream()
                .map(orderReadMapper::map)
                .toList();
    }

    @Transactional
    public boolean cancelOrder(long orderId) {
        var order = ordersRepository.findById(orderId);
        if (order.isEmpty()) throw new EntityNotFoundException("Can't find order with id : " + orderId);
        walletService.cancelRoom(order.get());
        ordersRepository.deleteById(orderId);
        return true;
    }

    public List<OrderReadDto> findOrdersByRoomId(long roomId) {
        return ordersRepository.findOrdersByRoom_Id(roomId)
                .stream()
                .map(orderReadMapper::map)
                .toList();
    }
}
