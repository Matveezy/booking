package booking.hotel.service;

import booking.hotel.dto.MoneyTransferRequest;
import booking.hotel.dto.MoneyTransferResponse;
import booking.hotel.dto.OrderCreateDto;
import booking.hotel.dto.OrderInfoDto;
import booking.hotel.entity.Order;
import booking.hotel.entity.Room;
import booking.hotel.exception.FailedOrderException;
import booking.hotel.feign.WalletServiceClient;
import booking.hotel.mapper.OrderReadMapper;
import booking.hotel.repo.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderReadMapper orderReadMapper;
    private final RoomService roomService;
    private final WalletServiceClient walletServiceClient;

    public OrderService(OrderRepository orderRepository, OrderReadMapper orderReadMapper, @Lazy RoomService roomService, WalletServiceClient walletServiceClient) {
        this.orderRepository = orderRepository;
        this.orderReadMapper = orderReadMapper;
        this.roomService = roomService;
        this.walletServiceClient = walletServiceClient;
    }

    @Transactional
    public OrderInfoDto makeOrder(Long userId, OrderCreateDto orderCreateDto) {
        Optional<Room> roomById = roomService.findRoomById(orderCreateDto.getRoomId());
        if (roomById.isEmpty())
            throw new EntityNotFoundException("Room with id " + orderCreateDto.getRoomId() + " is not found");
        if (roomService.isRoomFree(orderCreateDto.getRoomId(), orderCreateDto.getDateIn(), orderCreateDto.getDateOut())) {
            Long transferToUserId = roomById.get().getHotel().getUserId().get(0);
            ResponseEntity<MoneyTransferResponse> moneyTransferResponseResponseEntity = walletServiceClient.transferMoney(MoneyTransferRequest.builder()
                    .fromUserId(userId)
                    .toUserId(transferToUserId)
                    .amount(roomById.get().getPrice() * Duration.between(orderCreateDto.getDateIn(), orderCreateDto.getDateOut()).getSeconds() / (60 * 60 * 24))
                    .build());
            MoneyTransferResponse moneyTransferResponse = moneyTransferResponseResponseEntity.getBody();
            if (!moneyTransferResponse.isCompleted()) throw new FailedOrderException(moneyTransferResponse.getCause());
            Order orderEntityToSave = Order.builder()
                    .room(roomById.get())
                    .hotel(roomById.get().getHotel())
                    .userId(userId)
                    .dateIn(orderCreateDto.getDateIn())
                    .dateOut(orderCreateDto.getDateOut())
                    .createdAt(Instant.now())
                    .build();
            orderRepository.save(orderEntityToSave);
            return orderReadMapper.mapToDto(orderEntityToSave);
        } else throw new FailedOrderException("Room is not free");
    }

    public List<OrderInfoDto> getOrdersByRoomId(long roomId) {
        return orderRepository.findOrdersByRoomId(roomId).stream()
                .map(orderReadMapper::mapToDto)
                .toList();
    }
}
