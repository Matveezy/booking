package com.example.booking.service;

import com.example.booking.dto.BookRoomDto;
import com.example.booking.dto.OrderReadDto;
import com.example.booking.dto.RoomInfoDto;
import com.example.booking.dto.UserReadDto;
import com.example.booking.repository.OrdersRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import integration.annotation.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import java.time.Instant;
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
        var userOpt = userService.findUserByName("ivan@gmail.com");
        assertThat(userOpt).isPresent();

        var roomOptional = roomService.findRoom(1);
        assertTrue(roomOptional.isPresent());
        RoomInfoDto room = roomOptional.get();
        var userId = userOpt.get().getId();
        var bookRoomDto = BookRoomDto.builder()
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(200000))
                .userId(userId)
                .build();

        var balanceUserBefore = walletService.getUserBalance(userId);
        Optional<UserReadDto> hotelOwner = hotelService.getHotelOwner(roomOptional.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        var balanceOwnerBefore = walletService.getUserBalance(hotelOwner.get().getId());

        ordersService.makeOrder(bookRoomDto, room.getId());

        assertThat(ordersService.showOrders(userId))
                .contains(OrderReadDto.builder()
                        .dateIn(Instant.ofEpochSecond(10000))
                        .dateOut(Instant.ofEpochSecond(200000))
                        .room(roomService.findRoom(room.getId()).orElse(null))
                        .hotel(hotelService.findHotelInfo(room.getHotelInfoDto().getId()).orElse(null))
                        .userReadDto(userService.findUserById(userId).orElse(null))
                        .build()
                );

        var balanceUserAfter = walletService.getUserBalance(userId);
        hotelOwner = hotelService.getHotelOwner(roomOptional.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        var balanceOwnerAfter = walletService.getUserBalance(hotelOwner.get().getId());

        assertThat(balanceUserBefore + balanceOwnerBefore)
                .isEqualTo(balanceUserAfter + balanceOwnerAfter);

        assertThat(balanceUserBefore - balanceUserAfter)
                .isEqualTo(balanceOwnerAfter - balanceOwnerBefore);

        assertThat(balanceUserBefore).isGreaterThan(balanceUserAfter);
        assertThat(balanceOwnerBefore).isLessThan(balanceOwnerAfter);
    }

    @Test
    @WithMockCustomUser(username = "ivan@gmail.com")
    public void cancelOrderTest() {
        var userOpt = userService.findUserByName("ivan@gmail.com");
        assertThat(userOpt).isPresent();

        var room = roomService.findRoom(1);
        assertTrue(room.isPresent());
        assertThat(room).isNotNull();

        var userId = userOpt.get().getId();
        var bookRoomDto = BookRoomDto.builder()
                .dateIn(Instant.ofEpochSecond(10000))
                .dateOut(Instant.ofEpochSecond(200000))
                .userId(userId)
                .build();

        var balanceUserBefore = walletService.getUserBalance(userId);
        Optional<UserReadDto> hotelOwner = hotelService.getHotelOwner(room.get().getHotelInfoDto().getId());
        assertTrue(hotelOwner.isPresent());
        var balanceOwnerBefore = walletService.getUserBalance(hotelOwner.get().getId());

        ordersService.makeOrder(bookRoomDto, room.get().getId());

        var order = ordersRepository.findOrdersByUser_Id(userId).get(0);
        ordersService.cancelOrder(order.getId());
        assertThat(ordersService.showOrders(userId)).isEmpty();


        var balanceUserAfter = walletService.getUserBalance(userId);
        var balanceOwnerAfter = walletService.getUserBalance(hotelOwner.get().getId());

        assertThat(balanceUserBefore).isEqualTo(balanceUserAfter);
        assertThat(balanceOwnerBefore).isEqualTo(balanceOwnerAfter);
    }
}
