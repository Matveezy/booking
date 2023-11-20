package com.example.booking.controller;

import com.example.booking.dto.HotelCreateDto;
import com.example.booking.dto.HotelUpdateDto;
import com.example.booking.dto.RoomCreateDto;
import com.example.booking.dto.RoomUpdateDto;
import com.example.booking.service.HotelService;
import com.example.booking.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping("/hotel")
    @PreAuthorize("hasAnyAuthority('OWNER', 'ADMIN')")
    public ResponseEntity<?> createHotel(@Validated @RequestBody HotelCreateDto dto) {
        Long createdId = hotelService.saveHotel(dto);
        String response = "Created hotel id=" + createdId;
        return ResponseEntity.ok(response);
    }

    @PutMapping("/hotel/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'ADMIN')")
    public ResponseEntity<?> updateHotel(@Validated @RequestBody HotelUpdateDto dto,
                                         @PathVariable long id) {
        hotelService.updateHotel(dto, id);
        String response = "Hotel was successfully updated";
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hotel/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'ADMIN')")
    public ResponseEntity<?> deleteHotel(@PathVariable long id) {
        hotelService.deleteHotel(id);
        String response = "Hotel was successfully deleted";
        return ResponseEntity.ok(response);
    }

    @PostMapping("/room")
    @PreAuthorize("hasAnyAuthority('OWNER', 'ADMIN')")
    public ResponseEntity<?> createRoom(@Validated @RequestBody RoomCreateDto dto) {
        Long createdId = roomService.saveRoom(dto);
        String response = "Created room id=" + createdId;
        return ResponseEntity.ok(response);
    }

    @PutMapping("/room/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'ADMIN')")
    public ResponseEntity<?> updateRoom(@Validated @RequestBody RoomUpdateDto dto, @PathVariable long id) {
        roomService.updateRoom(dto, id);
        String response = "Room was successfully updated";
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/room/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable long id) {
        if (roomService.deleteRoom(id)) return ResponseEntity.ok().build();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
