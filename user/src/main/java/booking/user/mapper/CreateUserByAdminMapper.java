package booking.user.mapper;

import booking.user.dto.CreateUserByAdminDto;
import booking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserByAdminMapper implements Mapper<CreateUserByAdminDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserByAdminDto mapToDto(User entity) {
        return null;
    }

    @Override
    public User mapToEntity(CreateUserByAdminDto dto) {
        return User
                .builder()
                .login(dto.getLogin())
                .pass(passwordEncoder.encode(dto.getPass()))
                .name(dto.getName())
                .dateOfBirth(dto.getDateOfBirth())
                .role(dto.getRole())
                .build();
    }
}
