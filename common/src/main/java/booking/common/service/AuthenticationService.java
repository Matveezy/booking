package booking.common.service;

import booking.common.dto.AuthenticationRequest;
import booking.common.dto.AuthenticationResponse;
import booking.common.dto.UserInfoDto;
import booking.common.exception.EntityNotFoundException;
import booking.common.mapper.UserEntityMapper;
import booking.common.dto.RegisterRequest;
import booking.common.entity.User;
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
        User userEntity = userEntityMapper.map(UserInfoDto.builder().id(createdUserId).build());
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
