package com.example.booking.mapper;

import com.example.booking.dto.HotelCreateDto;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.User;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelEntityMapper implements Mapper<HotelCreateDto, Hotel> {

    private final UserRepository userRepository;

    @Override
    public Hotel map(HotelCreateDto hotelCreateDto) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> ownerOptional = userRepository.findByLogin(principal.getUsername());
        if (ownerOptional.isEmpty())
            throw new EntityNotFoundException("Can't find user with login : " + principal.getUsername());
        Hotel hotel = Hotel.builder()
                .name(hotelCreateDto.getName())
                .city(hotelCreateDto.getCity())
                .hotelClass(hotelCreateDto.getHotelClass())
                .owner(ownerOptional.get())
                .build();
        return hotel;
    }
}
