package booking.common.service;

import booking.common.dto.CreateUserDto;
import booking.common.dto.UserInfoDto;
import booking.common.entity.Role;
import booking.common.entity.User;
import booking.common.mapper.CreateUserMapper;
import booking.common.mapper.RegisterRequestMapper;
import booking.common.mapper.UserReadMapper;
import booking.common.dto.RegisterRequest;
import booking.common.repository.UserRepository;
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

    public Optional<UserInfoDto> findUserById(long id) {
        return userRepository.findUserById(id)
                .map(userReadMapper::map);
    }

    public Optional<UserInfoDto> findByLogin(String name) {
        return userRepository.findByLogin(name)
                .map(userReadMapper::map);
    }
}

