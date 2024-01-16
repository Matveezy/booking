package booking.user.service;

import booking.user.dto.CreateUserByAdminDto;
import booking.user.dto.RegisterRequest;
import booking.user.dto.UpdateUserDto;
import booking.user.dto.UserReadDto;
import booking.user.entity.User;
import booking.user.feign.WalletServiceClient;
import booking.user.mapper.CreateUserByAdminMapper;
import booking.user.mapper.RegisterRequestMapper;
import booking.user.mapper.UserReadMapper;
import booking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final RegisterRequestMapper registerRequestMapper;
    private final CreateUserByAdminMapper createUserByAdminMapper;
    private final WalletServiceClient walletServiceClient;

    @Transactional
    public UserReadDto createUser(RegisterRequest registerRequest) {
        User userEntity = registerRequestMapper.mapToEntity(registerRequest);
        userRepository.save(userEntity);
        walletServiceClient.createWallet(userEntity.getId());
        return userReadMapper.mapToDto(userEntity);
    }

    @Transactional
    public UserReadDto createUser(CreateUserByAdminDto createUserByAdminDto) {
        User userEntity = createUserByAdminMapper.mapToEntity(createUserByAdminDto);
        userRepository.save(userEntity);
        walletServiceClient.createWallet(userEntity.getId());
        return userReadMapper.mapToDto(userEntity);
    }

    @Transactional
    public UserReadDto updateUser(UpdateUserDto updateUserDto) {
        Optional<User> userEntity = userRepository.findByLogin(updateUserDto.getLogin());
        return userEntity.map(user -> {
                    user.setRole(updateUserDto.getRole());
                    userRepository.save(user);
                    return userReadMapper.mapToDto(user);
                }
        ).orElseThrow(() -> new UsernameNotFoundException("User with login : " + updateUserDto.getLogin() + "is not found!"));
    }

    public Optional<UserReadDto> findUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(userReadMapper::mapToDto);
    }

    public Optional<UserReadDto> findUserById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::mapToDto);
    }
}
