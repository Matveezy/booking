package com.example.booking.dto;

import com.example.booking.entity.Role;
import com.example.booking.validation.EnumNamePattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionUpdateDto {

    @NotNull(message = "login cannot be null!")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "login must be an email!")
    String login;

    @EnumNamePattern(regexp = "USER|OWNER|ADMIN",
            message = "role can take the following values: USER, OWNER, ADMIN")
    Role role;
}
