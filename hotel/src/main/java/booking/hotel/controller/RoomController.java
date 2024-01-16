package booking.hotel.controller;

import booking.hotel.dto.*;
import booking.hotel.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/rooms")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public ResponseEntity<RoomReadDto> createRoom(HttpServletRequest httpServletRequest,
                                                  @RequestBody @Validated RoomCreateDto roomCreateDto) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(roomService.createRoom(Long.valueOf(userId), roomCreateDto),
                HttpStatus.CREATED);
    }

    @PutMapping("/rooms")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public ResponseEntity<RoomReadDto> updateRoom(HttpServletRequest httpServletRequest,
                                                  @RequestBody @Validated RoomUpdateDto roomUpdateDto) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(roomService.updateRoom(Long.valueOf(userId), roomUpdateDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/rooms")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public ResponseEntity<?> deleteRoom(HttpServletRequest httpServletRequest,
                                        @RequestParam Long roomId) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return roomService.deleteRoom(Long.valueOf(userId), roomId) ? ResponseEntity.status(200).build()
                : ResponseEntity.badRequest().build();
    }
}
