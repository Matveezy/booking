package booking.mapper;

import booking.dto.UserReadDto;
import booking.entity.User;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class UserReadMapperTest extends IntegrationTestBase {
    private final UserReadMapper mapper;

    private final String LOGIN = "testlogin";
    @Test
    void map() {
        User user = User.builder()
                .login(LOGIN)
                .build();
        UserReadDto dto = mapper.map(user);
        assertThat(user.getLogin()).isEqualTo(dto.getLogin());
    }
}
