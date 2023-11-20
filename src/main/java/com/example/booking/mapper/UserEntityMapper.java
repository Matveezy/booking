package com.example.booking.mapper;

import com.example.booking.dto.UserReadDto;
import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEntityMapper implements Mapper<UserReadDto, User> {

    private final UserRepository userRepository;

    @Override
    public User map(UserReadDto userReadDto) {
        return userRepository.findUserById(userReadDto.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Can't retrieve user with id " + userReadDto.getId()));
    }
}
