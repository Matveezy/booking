package com.example.booking.controller;

import com.example.booking.dto.HotelCreateDto;
import com.example.booking.dto.HotelUpdateDto;
import com.example.booking.dto.RoomCreateDto;
import com.example.booking.dto.RoomUpdateDto;
import com.example.booking.service.HotelService;
import com.example.booking.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class OwnerController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping("/hotel")
    public ResponseEntity<String> createHotel(@Validated @RequestBody HotelCreateDto dto) {
        Long createdId = hotelService.saveHotel(dto);
        String response = "Created hotel id=" + createdId;
        return ResponseEntity.ok(response);
    }

    @PutMapping("/hotel/{id}")
    public ResponseEntity<String> updateHotel(@Validated @RequestBody HotelUpdateDto dto, @PathVariable long id) {
        hotelService.updateHotel(dto, id);
        String response = "Hotel was successfully updated";
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hotel/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable long id) {
        hotelService.deleteHotel(id);
        String response = "Hotel was successfully deleted";
        return ResponseEntity.ok(response);
    }

    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@Validated @RequestBody RoomCreateDto dto) {
        Long createdId = roomService.saveRoom(dto);
        String response = "Created room id=" + createdId;
        return ResponseEntity.ok(response);
    }

    @PutMapping("/room/{id}")
    public ResponseEntity<String> updateRoom(@Validated @RequestBody RoomUpdateDto dto, @PathVariable long id) {
        roomService.updateRoom(dto, id);
        String response = "Room was successfully updated";
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/room/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable long id) {
        roomService.deleteRoom(id);
        String response = "Hotel was successfully deleted";
        return ResponseEntity.ok(response);
    }
}
