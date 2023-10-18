package com.example.booking.service;

import com.example.booking.dto.AuthenticationRequest;
import com.example.booking.dto.AuthenticationResponse;
import com.example.booking.dto.RegisterRequest;
import com.example.booking.entity.Role;
import com.example.booking.entity.User;
import com.example.booking.mapper.UserCreateMapper;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserCreateMapper userCreateMapper;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User userEntityToSave = userCreateMapper.map(request);
        userRepository.save(userEntityToSave);
        String jwt = jwtService.generateToken(userEntityToSave);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(), request.getPass()
                )
        );
        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user"));
        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
