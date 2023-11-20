package com.example.booking.service;

import com.example.booking.dto.CreateUserDto;
import com.example.booking.dto.RegisterRequest;
import com.example.booking.dto.UserReadDto;
import com.example.booking.entity.Role;
import com.example.booking.entity.User;
import com.example.booking.mapper.CreateUserMapper;
import com.example.booking.mapper.RegisterRequestMapper;
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
    private final CreateUserMapper createUserMapper;
    private final RegisterRequestMapper registerRequestMapper;

    @Transactional
    public long createUser(CreateUserDto request) {
        User userToCreate = createUserMapper.map(request);
        userRepository.save(userToCreate);
        return userToCreate.getId();
    }

    @Transactional
    public long createUser(RegisterRequest request) {
        User userToCreate = registerRequestMapper.map(request);
        userRepository.save(userToCreate);
        return userToCreate.getId();
    }

    @Transactional
    public int updateRole(Role roleToUpdate, String userLogin) {
        return userRepository.updateRole(roleToUpdate, userLogin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    public Optional<UserReadDto> findUserById(long id) {
        return userRepository.findUserById(id)
                .map(userReadMapper::map);
    }

    public Optional<UserReadDto> findByLogin(String name) {
        return userRepository.findByLogin(name)
                .map(userReadMapper::map);
    }
}

