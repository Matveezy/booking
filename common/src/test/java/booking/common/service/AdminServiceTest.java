package booking.common.service;

import booking.common.dto.UserPermissionUpdateDto;
import booking.common.entity.Role;
import booking.common.entity.User;
import booking.common.repository.UserRepository;
import booking.common.service.AdminService;
import booking.common.service.UserService;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
public class AdminServiceTest extends IntegrationTestBase {

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
