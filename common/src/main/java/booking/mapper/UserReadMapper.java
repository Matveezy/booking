package booking.mapper;

import booking.dto.UserReadDto;
import booking.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User from) {
        return UserReadDto.builder()
                .id(from.getId())
                .login(from.getLogin())
                .name(from.getName())
                .role(from.getRole())
                .build();
    }
}