package com.example.booking.dto;

import com.example.booking.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto {

    String login;
    String name;
    Role role;
}
