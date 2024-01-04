package booking.service;

import booking.dto.CreateOrderDto;
import booking.dto.UserReadDto;
import booking.exception.InsufficientMoneyBalanceException;
import booking.dto.*;
import booking.exception.InsufficientMoneyBalanceException;
import booking.repository.OrdersRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import integration.annotation.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
public class OrderServiceTest extends IntegrationTestBase {

    private final WalletService walletService;
    private final OrdersService ordersService;
    private final UserService userService;
    private final RoomService roomService;
    private final HotelService hotelService;
    private final OrdersRepository ordersRepository;

    @Test
    @WithMockCustomUser(username = "ivan@gmail.com")
    public void makeOrderTest() {
        var userOpt = userService.findByLogin("ivan@gmail.com");
        assertThat(userOpt).isPresent();

        var roomOptional = roomService.findRoom(1);
        assertTrue(roomOptional.isPresent());
        RoomInfoDto room = roomOptional.get();
        var userId = userOpt.get().getId();
        long twoDaysSecond = Duration.of(2, ChronoUnit.DAYS).toSeconds();
        var createOrderDto = CreateOrderDto.builder()
                .roomId(room.getId())
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(10000 + twoDaysSecond))
                .userId(userId)
                .build();

        var balanceUserBefore = walletService.getUserBalance(userId);
        Optional<UserReadDto> hotelOwner = hotelService.getHotelOwner(roomOptional.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        var balanceOwnerBefore = walletService.getUserBalance(hotelOwner.get().getId());

        ordersService.makeOrder(createOrderDto);

        assertThat(ordersService.showOrders(userId))
                .contains(OrderReadDto.builder()
                        .dateIn(Instant.ofEpochSecond(10000))
                        .dateOut(Instant.ofEpochSecond(10000 + twoDaysSecond))
                        .room(roomService.findRoom(room.getId()).orElse(null))
                        .hotel(hotelService.findHotelInfo(room.getHotelInfoDto().getId()).orElse(null))
                        .userReadDto(userService.findUserById(userId).orElse(null))
                        .build()
                );

        var balanceUserAfter = walletService.getUserBalance(userId);
        hotelOwner = hotelService.getHotelOwner(roomOptional.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        var balanceOwnerAfter = walletService.getUserBalance(hotelOwner.get().getId());

        Assertions.assertThat(balanceUserBefore + balanceOwnerBefore)
                .isEqualTo(balanceUserAfter + balanceOwnerAfter);

        Assertions.assertThat(balanceUserBefore - balanceUserAfter)
                .isEqualTo(balanceOwnerAfter - balanceOwnerBefore);

        assertThat(balanceUserBefore).isGreaterThan(balanceUserAfter);
        assertThat(balanceOwnerBefore).isLessThan(balanceOwnerAfter);
    }

    @Test
    @WithMockCustomUser(username = "ivan@gmail.com")
    public void cancelOrderTest() {
        var userOpt = userService.findByLogin("ivan@gmail.com"); // balance 10000
        assertThat(userOpt).isPresent();

        var room = roomService.findRoom(2); // price 2500
        assertTrue(room.isPresent());
        assertThat(room).isNotNull();
        long twoDaysSeconds = Duration.of(2, ChronoUnit.DAYS).toSeconds();
        var userId = userOpt.get().getId();
        var createOrderDto = CreateOrderDto.builder()
                .roomId(room.get().getId())
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(10000 + twoDaysSeconds))
                .userId(userId)
                .build();

        var balanceUserBefore = walletService.getUserBalance(userId);
        Optional<UserReadDto> hotelOwner = hotelService.getHotelOwner(room.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        var balanceOwnerBefore = walletService.getUserBalance(hotelOwner.get().getId());

        ordersService.makeOrder(createOrderDto);

        var order = ordersRepository.findOrdersByUser_Id(userId).get(0);
        ordersService.cancelOrder(order.getId());
        assertThat(ordersService.showOrders(userId)).isEmpty();


        var balanceUserAfter = walletService.getUserBalance(userId);
        var balanceOwnerAfter = walletService.getUserBalance(hotelOwner.get().getId());

        assertThat(balanceUserBefore).isEqualTo(balanceUserAfter);
        assertThat(balanceOwnerBefore).isEqualTo(balanceOwnerAfter);
    }

    @Test
    @WithMockCustomUser(username = "ivan@gmail.com")
    public void makeOrderNotEnoughMoneyTest() {
        var userOpt = userService.findByLogin("ivan@gmail.com"); // balance 10000
        assertThat(userOpt).isPresent();

        var room = roomService.findRoom(1); // price 4000
        assertTrue(room.isPresent());
        assertThat(room).isNotNull();
        long threeDaysSeconds = Duration.of(3, ChronoUnit.DAYS).toSeconds();

        var userId = userOpt.get().getId();
        var createOrderDto = CreateOrderDto.builder()
                .roomId(room.get().getId())
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(10000 + threeDaysSeconds))
                .userId(userId)
                .build();

        Optional<UserReadDto> hotelOwner = hotelService.getHotelOwner(room.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        assertThrows(InsufficientMoneyBalanceException.class, () -> ordersService.makeOrder(createOrderDto));
    }
}
