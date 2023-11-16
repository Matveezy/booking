package com.example.booking.service;

import com.example.booking.dto.*;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.HotelClass;
import com.example.booking.entity.User;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.mapper.HotelReadMapper;
import com.example.booking.mapper.UserReadMapper;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {
    public final static int MAX_PAGE_SIZE = 10;

    private final HotelRepository hotelRepo;
    private final UserRepository userRepository;
    private final HotelReadMapper hotelReadMapper;
    private final UserReadMapper userReadMapper;

    public Optional<HotelInfoDto> findHotelInfo(long id) {
        return hotelRepo.findHotelById(id)
                .map(hotelReadMapper::map);
    }

    public List<HotelInfoDto> findHotels(int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return hotelRepo.findAll(pageable).stream()
                .map(hotelReadMapper::map)
                .toList();
    }

    public List<HotelInfoDto> findHotelsByCity(String city, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return hotelRepo.findByCity(city, pageable).stream()
                .map(hotelReadMapper::map)
                .toList();
    }

    public List<HotelInfoDto> findHotelsByCityAndHotelClass(String city, HotelClass hotelClass, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return hotelRepo.findByCityAndHotelClass(city, hotelClass, pageable).stream()
                .map(hotelReadMapper::map)
                .toList();
    }

    public List<HotelInfoDto> findHotels(HotelsFilterSearchDto filter, int page) {
        if (filter.getCity() == null && filter.getHotelClass() == null) {
            return findHotels(page);
        } else if (filter.getHotelClass() == null) {
            return findHotelsByCity(filter.getCity(), page);
        } else {
            return findHotelsByCityAndHotelClass(filter.getCity(), filter.getHotelClass(), page);
        }
    }

    public Optional<UserReadDto> getHotelOwner(Long hotelId) {
        return hotelRepo.findHotelById(hotelId)
                .map(hotelEntity -> userReadMapper.map(hotelEntity.getOwner()));
    }

    @Transactional
    public Long saveHotel(HotelCreateDto hotelCreateDto) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalOwner = userRepository.findByLogin(principal.getUsername());
        User owner = optionalOwner.get();

        Hotel hotel = Hotel.builder()
                .name(hotelCreateDto.getName())
                .city(hotelCreateDto.getCity())
                .hotelClass(hotelCreateDto.getHotelClass())
                .owner(owner)
                .build();

        Hotel savedHotel = hotelRepo.save(hotel);
        return savedHotel.getId();
    }

    @Transactional
    public void updateHotel(HotelUpdateDto hotelDto, long hotelId) throws AccessDeniedException {
        Optional<Hotel> optionalHotel = hotelRepo.findById(hotelId);
        if (optionalHotel.isEmpty()) {
            throw new HotelNotFoundException(hotelId);
        }
        Hotel hotel = optionalHotel.get();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
            hotel.setName(hotelDto.getName());
            hotel.setCity(hotelDto.getCity());
            hotel.setHotelClass(hotelDto.getHotelClass());

            hotelRepo.save(hotel);
        } else {
            throw new AccessDeniedException("You do not have permission to modify this hotel");
        }
    }

    @Transactional
    public void deleteHotel(long hotelId) throws AccessDeniedException {
        if (hotelRepo.existsById(hotelId)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Hotel hotel = hotelRepo.findHotelById(hotelId).get();
            if (Objects.equals(hotel.getOwner().getLogin(), principal.getUsername())) {
                hotelRepo.deleteById(hotelId);
            } else {
                throw new AccessDeniedException("You do not have permission to delete this hotel");
            }
        } else {
            throw new HotelNotFoundException(hotelId);
        }
    }
}
