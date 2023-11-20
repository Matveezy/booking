package com.example.booking.mapper;

import com.example.booking.dto.CreateUserDto;
import com.example.booking.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CreateUserMapper implements Mapper<CreateUserDto, User> {

    @Override
    public User map(CreateUserDto request) {
        return User.builder()
                .login(request.getLogin())
                .pass(request.getPass())
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .role(request.getRole())
                .build();
    }
}
