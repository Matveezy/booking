package booking.hotel.controller;

import booking.hotel.dto.HotelCreateDto;
import booking.hotel.dto.HotelInfoDto;
import booking.hotel.dto.HotelUpdateDto;
import booking.hotel.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping("/hotels")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public ResponseEntity<HotelInfoDto> createHotel(HttpServletRequest httpServletRequest,
                                                    @RequestBody @Validated HotelCreateDto hotelCreateDto) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(hotelService.createHotel(Long.valueOf(userId), hotelCreateDto),
                HttpStatus.CREATED);
    }

    @PutMapping("/hotels")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public ResponseEntity<HotelInfoDto> updateHotel(HttpServletRequest httpServletRequest,
                                                    @RequestBody @Validated HotelUpdateDto hotelUpdateDto) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(hotelService.updateHotel(Long.valueOf(userId), hotelUpdateDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/hotels")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public ResponseEntity<?> deleteHotel(HttpServletRequest httpServletRequest,
                                                    @RequestParam Long hotelId) {
        String userId = httpServletRequest.getHeader("userId");
        if (userId == null) return ResponseEntity.badRequest().build();
        return hotelService.deleteHotel(Long.valueOf(userId), hotelId) ? ResponseEntity.status(200).build()
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/hotels")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<HotelInfoDto>> findAll() {
        return ResponseEntity.ok(
                hotelService.findAll());
    }
}
