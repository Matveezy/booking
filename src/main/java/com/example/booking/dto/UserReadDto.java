package com.example.booking.dto;

import com.example.booking.entity.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto {

    String login;
    String name;
    Role role;
}
