package com.example.booking.mapper;

import com.example.booking.dto.UserReadDto;
import com.example.booking.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User from) {
        return UserReadDto.builder()
                .login(from.getLogin())
                .name(from.getName())
                .role(from.getRole())
                .build();
    }
}