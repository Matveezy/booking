package booking.common.mapper;

import booking.common.dto.UserInfoDto;
import booking.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserInfoDto> {

    @Override
    public UserInfoDto map(User from) {
        return UserInfoDto.builder()
                .id(from.getId())
                .login(from.getLogin())
                .name(from.getName())
                .role(from.getRole())
                .build();
    }
}