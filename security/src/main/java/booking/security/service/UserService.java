package booking.security.service;

import booking.security.dto.RegisterRequest;
import booking.security.dto.UserReadDto;
import booking.security.feign.UserServiceClient;
import booking.security.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserServiceClient userServiceClient;
    private final UserReadMapper userReadMapper;

    public Optional<UserReadDto> findUserByLogin(String login) {
        return Optional.of(userServiceClient.findUserByLogin(login));
    }

    @Transactional
    public UserReadDto createUser(RegisterRequest request) {
        return userServiceClient.createUser(request);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userReadMapper.mapToEntity(userServiceClient.findUserByLogin(username));
    }
}
