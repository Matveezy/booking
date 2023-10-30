package com.example.booking.dto;

import com.example.booking.entity.Room;
import com.example.booking.entity.RoomClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomInfoDto {
    private RoomClass roomClass;
    private Long price;

    public RoomInfoDto(Room room) {
        this.roomClass = room.getRoomClass();
        this.price = room.getPrice();
    }
}
