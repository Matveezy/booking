package com.example.booking.controller;

import com.example.booking.dto.*;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.service.HotelService;
import com.example.booking.service.OrdersService;
import com.example.booking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final HotelService hotelService;
    private final RoomService roomService;
    private final OrdersService ordersService;

    @GetMapping("/info/hotel/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public HotelInfoDto getHotelInfo(@PathVariable long id) {
        return hotelService.findHotelInfo(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/info/hotels")
    @PreAuthorize("hasAuthority('USER')")
    public List<HotelInfoDto> getHotelInfo(@Validated @RequestBody HotelsFilterSearchDto filter, @RequestParam int page) {
        return hotelService.findHotels(filter, page);
    }

    @PostMapping("/info/rooms/{hotelId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<RoomInfoDto> getRoomsInfo(@Validated @RequestBody RoomsFilterSearchDto filter, @PathVariable long hotelId, @RequestParam int page) {
        return roomService.findRoomsByDates(filter, hotelId, page);
    }

    @PostMapping("/order/{roomId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> bookRoom(@Validated @RequestBody BookRoomDto order, @PathVariable long roomId) {
        return ordersService.makeOrder(order, roomId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/orders/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderReadDto> showOrders(@PathVariable long userId) {
        return ordersService.showOrders(userId);
    }

    @PostMapping("/cancel/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> cancelOrder(@PathVariable long orderId) {
        if (ordersService.cancelOrder(orderId)) return ResponseEntity.ok().build();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
