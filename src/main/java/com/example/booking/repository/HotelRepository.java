package com.example.booking.repository;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.HotelClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long>, CrudRepository<Hotel, Long> {
    Optional<Hotel> findHotelById(long id);
    List<Hotel> findByCity(String city, Pageable pageable);
    List<Hotel> findByHotelClass(HotelClass hotelClass, Pageable pageable);
    List<Hotel> findByCityAndHotelClass(String city, HotelClass hotelClass, Pageable pageable);
}
