package com.example.booking.dto;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.HotelClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelInfoDto {

    private Long id;
    private String name;
    private String city;
    private HotelClass hotelClass;
    private Long userScore;

    public HotelInfoDto(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.city = hotel.getCity();
        this.hotelClass = hotel.getHotelClass();
        this.userScore = hotel.getUserScore();
    }
}
