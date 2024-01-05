package booking.common.service;

import booking.common.dto.CreateOrderDto;
import booking.common.exception.EntityNotFoundException;
import booking.common.exception.NotEnoughPermissionException;
import booking.common.exception.RoomIsNotFreeException;
import booking.common.mapper.OrderReadMapper;
import booking.common.mapper.RoomReadMapper;
import booking.common.dto.OrderInfoDto;
import booking.common.entity.Order;
import booking.common.mapper.OrderEntityMapper;
import booking.common.repository.OrdersRepository;
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


    public List<OrderInfoDto> showOrders(long userId) {
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

    public List<OrderInfoDto> findOrdersByRoomId(long roomId) {
        return ordersRepository.findOrdersByRoom_Id(roomId)
                .stream()
                .map(orderReadMapper::map)
                .toList();
    }
}
