package booking.mapper;

import booking.dto.RegisterRequest;
import booking.entity.Role;
import booking.entity.User;
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
