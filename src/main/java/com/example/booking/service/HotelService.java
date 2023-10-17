package com.example.booking.service;

import com.example.booking.dto.HotelCreateDto;
import com.example.booking.dto.HotelInfoDto;
import com.example.booking.dto.HotelUpdateDto;
import com.example.booking.dto.HotelsFilterSearchDto;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.HotelClass;
import com.example.booking.entity.User;
import com.example.booking.exception.HotelNotFoundException;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class HotelService {
    public final static int MAX_PAGE_SIZE = 10;

    private final HotelRepository hotelRepo;
    private final UserRepository userRepository;

    public HotelInfoDto findHotelInfo(long id) {
        var hotel = hotelRepo.findHotelById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return new HotelInfoDto(hotel);
    }

    public List<HotelInfoDto> findHotels(int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return hotelRepo.findAll(pageable).stream()
                .map(HotelInfoDto::new)
                .toList();
    }

    public List<HotelInfoDto> findHotelsByCity(String city, int page) {
        Pageable pageable = PageRequest.of(page, MAX_PAGE_SIZE);
        return hotelRepo.findByCity(city, pageable).stream()
                .map(HotelInfoDto::new)
                .toList();
    }

    public List<HotelInfoDto> findHotelsByCityAndHotelClass(String city, HotelClass hotelClass, int page) {
        Pageable pageable = PageRequest.of(0, MAX_PAGE_SIZE);
        return hotelRepo.findByCityAndHotelClass(city, hotelClass, pageable).stream()
                .map(HotelInfoDto::new)
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

    public void deleteHotel(long hotelId) {
        if (hotelRepo.existsById(hotelId)) {
            hotelRepo.deleteById(hotelId);
        } else {
            throw new HotelNotFoundException(hotelId);
        }
    }
}
