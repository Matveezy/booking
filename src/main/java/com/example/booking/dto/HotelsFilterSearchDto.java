package com.example.booking.dto;

import com.example.booking.entity.HotelClass;
import jakarta.annotation.Nullable;
import lombok.*;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelsFilterSearchDto {
    private String city;
    private HotelClass hotelClass;
}
