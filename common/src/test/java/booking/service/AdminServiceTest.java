package booking.service;

import booking.dto.UserPermissionUpdateDto;
import booking.dto.UserPermissionUpdateDto;
import booking.entity.Role;
import booking.entity.User;
import booking.repository.UserRepository;
import booking.service.AdminService;
import booking.service.UserService;
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
