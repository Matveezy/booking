package booking.security.mapper;

import booking.security.dto.UserReadDto;
import booking.security.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<UserReadDto, User> {

    @Override
    public UserReadDto mapToDto(User entity) {
        return null;
    }

    @Override
    public User mapToEntity(UserReadDto dto) {
        return User.builder()
                .id(dto.getId())
                .login(dto.getLogin())
                .pass(dto.getPass())
                .role(dto.getRole())
                .build();
    }
}
