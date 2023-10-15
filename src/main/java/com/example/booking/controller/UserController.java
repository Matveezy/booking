package com.example.booking.controller;

import com.example.booking.dto.HotelInfoDto;
import com.example.booking.dto.HotelsFilterSearchDto;
import com.example.booking.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final HotelService hotelService;

    @GetMapping("/info/hotel/{id}")
    public HotelInfoDto getHotelInfo(@PathVariable long id) {
        return hotelService.findHotelInfo(id);
    }

    @GetMapping("/info/hotels")
    public List<HotelInfoDto> getHotelInfo(@Validated @RequestBody HotelsFilterSearchDto filter, @RequestParam int page) {
        return hotelService.findHotels(filter, page);
    }



}
