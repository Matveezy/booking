package integration;

import com.example.booking.dto.UserPermissionUpdateDto;
import com.example.booking.entity.Role;
import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.AdminService;
import com.example.booking.service.UserService;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@Sql("classpath:sql/data.sql")
public class AdminServiceIT extends IntegrationTestBase {

    private final AdminService adminService;
    private final UserService userService;
    private final UserRepository userRepository;


    @Test
    void updatePermission() {
        UserPermissionUpdateDto updateRoleRequestDto = UserPermissionUpdateDto.builder()
                .login("jackson@gmail.com")
                .role(Role.USER)
                .build();
        UserDetails userDetails = userService.loadUserByUsername(updateRoleRequestDto.getLogin());
        adminService.updatePermission(updateRoleRequestDto);
        Optional<User> maybeUpdatedUser = userRepository.findByLogin(updateRoleRequestDto.getLogin());
        maybeUpdatedUser.ifPresentOrElse(user -> {
                    assertEquals(user.getRole(), updateRoleRequestDto.getRole());
                },
                () -> assertFalse(false)
        );
    }
}
