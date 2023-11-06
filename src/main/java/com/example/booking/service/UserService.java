package com.example.booking.service;

import com.example.booking.dto.UserReadDto;
import com.example.booking.entity.User;
import com.example.booking.mapper.UserReadMapper;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    public Optional<UserReadDto> findUserById(long id) {
        return userRepository.findUserById(id)
                .map(userReadMapper::map);
    }

    public Optional<User> findUserEntityById(long id) {
        return userRepository.findUserById(id);
    }
}
