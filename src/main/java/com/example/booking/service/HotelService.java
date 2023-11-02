package com.example.booking.service;

import com.example.booking.dto.HotelCreateDto;
import com.example.booking.dto.HotelInfoDto;
import com.example.booking.dto.HotelUpdateDto;
import com.example.booking.dto.HotelsFilterSearchDto;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.HotelClass;
import com.example.booking.entity.User;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.mapper.HotelReadMapper;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {
    public final static int MAX_PAGE_SIZE = 10;

    private final HotelRepository hotelRepo;
    private final UserRepository userRepository;
    private final HotelReadMapper hotelReadMapper;

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

    @Transactional
    public Long saveHotel(HotelCreateDto hotelCreateDto) {
        Optional<User> optionalOwner = userRepository.findById(hotelCreateDto.getOwnerId());
        User owner = optionalOwner.get();

        Hotel hotel = new Hotel();
        hotel.setName(hotelCreateDto.getName());
        hotel.setCity(hotelCreateDto.getCity());
        hotel.setHotelClass(hotelCreateDto.getHotelClass());
        hotel.setOwner(owner);

        Hotel savedHotel = hotelRepo.save(hotel);
        return savedHotel.getId();
    }

    @Transactional
    public void updateHotel(HotelUpdateDto hotelDto, long hotelId) {
        Optional<Hotel> optionalHotel = hotelRepo.findById(hotelId);
        if (optionalHotel.isEmpty()) {
            throw new HotelNotFoundException(hotelId);
        }
        Hotel hotel = optionalHotel.get();

        hotel.setName(hotelDto.getName());
        hotel.setCity(hotelDto.getCity());
        hotel.setHotelClass(hotelDto.getHotelClass());

        hotelRepo.save(hotel);
    }

    @Transactional
    public void deleteHotel(long hotelId) {
        if (hotelRepo.existsById(hotelId)) {
            hotelRepo.deleteById(hotelId);
        } else {
            throw new HotelNotFoundException(hotelId);
        }
    }
}
