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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    public Mono<UserReadDto> createUser(RegisterRequest registerRequest) {
        User userEntity = registerRequestMapper.mapToEntity(registerRequest);
        return Mono.fromCallable(() -> userRepository.save(userEntity))
                .doOnNext(user -> walletServiceClient.createWallet(user.getId()))
                .map(userReadMapper::mapToDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<UserReadDto> createUser(CreateUserByAdminDto createUserByAdminDto) {
        User userEntity = createUserByAdminMapper.mapToEntity(createUserByAdminDto);
        return Mono.fromCallable(() -> userRepository.save(userEntity))
                .doOnNext(user -> walletServiceClient.createWallet(user.getId()))
                .map(userReadMapper::mapToDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<UserReadDto> updateUser(UpdateUserDto updateUserDto) {
        return Mono.fromCallable(() -> userRepository.findByLogin(updateUserDto.getLogin()))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with login : " + updateUserDto.getLogin() + "is not found!")))
                .flatMap(user -> {
                    user.setRole(updateUserDto.getRole());
                    return Mono.just(user);
                })
                .doOnNext(userRepository::save)
                .map(userReadMapper::mapToDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<UserReadDto> findUserByLogin(String login) {
        return Mono.fromCallable(() -> userRepository.findByLogin(login))
                .switchIfEmpty(Mono.error(new EntityNotFoundException()))
                .map(userReadMapper::mapToDto)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
