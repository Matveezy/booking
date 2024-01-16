package booking.user.mapper;

import booking.user.dto.RegisterRequest;
import booking.user.entity.Role;
import booking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterRequestMapper implements Mapper<RegisterRequest, User> {

    private final PasswordEncoder passwordEncoder;
    private final Role DEFAULT_CREATED_USER_ROLE = Role.USER;

    @Override
    public RegisterRequest mapToDto(User entity) {
        return null;
    }

    @Override
    public User mapToEntity(RegisterRequest dto) {
        return User.builder()
                .name(dto.getName())
                .login(dto.getLogin())
                .pass(passwordEncoder.encode(dto.getPass()))
                .dateOfBirth(dto.getDateOfBirth())
                .role(DEFAULT_CREATED_USER_ROLE)
                .build();
    }
}
