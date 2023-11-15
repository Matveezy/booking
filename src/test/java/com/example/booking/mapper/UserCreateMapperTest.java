package com.example.booking.mapper;

import com.example.booking.dto.RegisterRequest;
import com.example.booking.entity.User;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class UserCreateMapperTest extends IntegrationTestBase {
    private final UserCreateMapper mapper;

    private final String TEST_LOGIN = "testlogin";
    private final String TEST_PASSWORD = "testpassword";
    @Test
    void map() {
        RegisterRequest request = RegisterRequest.builder().login(TEST_LOGIN).pass(TEST_PASSWORD).build();
        User user = mapper.map(request);
        assertThat(user.getLogin()).isEqualTo(TEST_LOGIN);
    }
}
