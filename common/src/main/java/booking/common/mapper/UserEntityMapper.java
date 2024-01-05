package booking.common.mapper;

import booking.common.dto.UserInfoDto;
import booking.common.entity.User;
import booking.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEntityMapper implements Mapper<UserInfoDto, User> {

    private final UserRepository userRepository;

    @Override
    public User map(UserInfoDto userReadDto) {
        return userRepository.findUserById(userReadDto.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Can't retrieve user with id " + userReadDto.getId()));
    }
}
