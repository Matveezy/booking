package com.example.booking.controller;

import com.example.booking.dto.*;
import com.example.booking.entity.Order;
import com.example.booking.service.HotelService;
import com.example.booking.service.OrdersService;
import com.example.booking.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final HotelService hotelService;
    private final RoomService roomService;
    private final OrdersService ordersService;

    @GetMapping("/info/hotel/{id}")
    public HotelInfoDto getHotelInfo(@PathVariable long id) {
        return hotelService.findHotelInfo(id);
    }

    @GetMapping("/info/hotels")
    public List<HotelInfoDto> getHotelInfo(@Validated @RequestBody HotelsFilterSearchDto filter, @RequestParam int page) {
        return hotelService.findHotels(filter, page);
    }

    @PostMapping("/info/rooms/{hotelId}")
    public List<RoomInfoDto> getRoomsInfo(@Validated @RequestBody RoomsFilterSearchDto filter, @PathVariable long hotelId, @RequestParam int page) {
        return roomService.findRoomsByDates(filter, hotelId, page);
    }

    @PostMapping("/order/{roomId}")
    public void bookRoom(@Validated @RequestBody BookRoomDto order, @PathVariable long roomId) {
        ordersService.makeOrder(order, roomId);
    }

    @GetMapping("/orders/{userId}")
    public List<OrderDto> showOrders(@PathVariable long userId) {
        return ordersService.showOrders(userId);
    }

    @PostMapping("/cancel/{orderId}") //todo checks
    public void cancelOrder(@PathVariable long orderId) {
        ordersService.cancelOrder(orderId);
    }

}
