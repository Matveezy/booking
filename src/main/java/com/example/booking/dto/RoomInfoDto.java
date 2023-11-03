package com.example.booking.dto;

import com.example.booking.entity.RoomClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomInfoDto {

    private long roomNumber;
    private RoomClass roomClass;
    private long price;
    private HotelInfoDto hotelInfoDto;
}
