package booking.controller;

import booking.dto.CreateUserDto;
import booking.dto.UserPermissionUpdateDto;
import booking.service.AdminService;
import booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @PutMapping("/users/permission")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updatePermission(@RequestBody @Validated UserPermissionUpdateDto userPermissionUpdateDto) {
        adminService.updatePermission(userPermissionUpdateDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/users/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody @Validated CreateUserDto createUserDto) {
        return userService.createUser(createUserDto) > 0
                ? ResponseEntity.ok(HttpStatus.CREATED)
                : ResponseEntity.badRequest().build();
    }
}