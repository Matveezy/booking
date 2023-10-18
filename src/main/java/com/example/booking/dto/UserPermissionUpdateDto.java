package com.example.booking.dto;

import com.example.booking.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionUpdateDto {

    String login;
    Role role;
}
