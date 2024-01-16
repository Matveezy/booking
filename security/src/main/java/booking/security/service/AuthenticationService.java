package booking.security.service;

import booking.security.dto.AuthenticationRequest;
import booking.security.dto.AuthenticationResponse;
import booking.security.dto.RegisterRequest;
import booking.security.dto.UserReadDto;
import booking.security.entity.User;
import booking.security.mapper.UserReadMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserReadMapper userReadMapper;

    @Transactional
    public Optional<AuthenticationResponse> register(RegisterRequest request) {
        UserReadDto createUserDto = userService.createUser(request);
        String jwt = jwtService.generateToken(userReadMapper.mapToEntity(createUserDto));
        return Optional.of(AuthenticationResponse.builder()
                .userId(createUserDto.getId())
                .token(jwt)
                .build());
    }

    public Optional<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(), request.getPass()
                )
        );
        UserReadDto userByLogin = userService.findUserByLogin(request.getLogin())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user"));
        String jwt = jwtService.generateToken(userReadMapper.mapToEntity(userByLogin));
        return Optional.of(AuthenticationResponse.builder()
                .userId(userByLogin.getId())
                .token(jwt)
                .build());
    }
}
