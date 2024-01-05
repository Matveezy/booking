package booking.common.mapper;

import booking.common.dto.RegisterRequest;
import booking.common.entity.Role;
import booking.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterRequestMapper implements Mapper<RegisterRequest, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(RegisterRequest object) {
        return User.builder()
                .login(object.getLogin())
                .name(object.getName())
                .pass(passwordEncoder.encode(object.getPass()))
                .dateOfBirth(object.getDateOfBirth())
                .role(Role.USER)
                .build();
    }
}
