package booking.user.mapper;

import booking.user.dto.UserReadDto;
import booking.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<UserReadDto, User> {

    @Override
    public UserReadDto mapToDto(User entity) {
        return UserReadDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .pass(entity.getPass())
                .name(entity.getName())
                .role(entity.getRole())
                .build();
    }
}
