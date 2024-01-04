package booking.service;

import booking.dto.AuthenticationRequest;
import booking.dto.AuthenticationResponse;
import booking.dto.UserReadDto;
import booking.exception.EntityNotFoundException;
import booking.mapper.UserEntityMapper;
import booking.dto.RegisterRequest;
import booking.entity.User;
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
    private final UserEntityMapper userEntityMapper;

    @Transactional
    public Optional<AuthenticationResponse> register(RegisterRequest request) {
        long createdUserId = userService.createUser(request);
        User userEntity = userEntityMapper.map(UserReadDto.builder().id(createdUserId).build());
        String jwt = jwtService.generateToken(userEntity);
        return Optional.of(AuthenticationResponse.builder()
                .token(jwt)
                .build());
    }

    public Optional<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(), request.getPass()
                )
        );
        User userEntity = userService.findByLogin(request.getLogin())
                .map(userEntityMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user"));
        String jwt = jwtService.generateToken(userEntity);
        return Optional.of(AuthenticationResponse.builder()
                .token(jwt)
                .build());
    }
}
