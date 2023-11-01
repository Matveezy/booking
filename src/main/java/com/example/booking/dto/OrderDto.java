package com.example.booking.dto;

import com.example.booking.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Instant dateIn;
    private Instant dateOut;
    private HotelInfoDto hotel;
    private RoomInfoDto room;

    public OrderDto(Order order) {
        this.dateIn = order.getDateIn();
        this.dateOut = order.getDateOut();
        this.hotel = new HotelInfoDto(order.getHotel());
        this.room = new RoomInfoDto(order.getRoom());
    }
}
