package com.example.booking.mapper;

import com.example.booking.dto.RegisterRequest;
import com.example.booking.entity.Role;
import com.example.booking.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<RegisterRequest, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(RegisterRequest object) {
        return User.builder()
                .login(object.getLogin())
                .name(object.getName())
                .pass(passwordEncoder.encode(object.getPass()))
                .dateOfBirth(object.getDateOfBirth())
                .role(Role.USER)
                .build();
    }
}
