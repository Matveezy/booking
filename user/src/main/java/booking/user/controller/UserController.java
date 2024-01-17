package booking.user.controller;

import booking.user.dto.CreateUserByAdminDto;
import booking.user.dto.RegisterRequest;
import booking.user.dto.UpdateUserDto;
import booking.user.dto.UserReadDto;
import booking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserReadDto createUser(@RequestBody @Validated CreateUserByAdminDto createUserByAdminDto) {
        return userService.createUser(createUserByAdminDto).block();
    }

    @PutMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserReadDto updateUser(@RequestBody @Validated UpdateUserDto updateUserDto) {
        return userService.updateUser(updateUserDto).block();
    }

    @PostMapping("/users")
    @PreAuthorize("permitAll()")
    public UserReadDto createUser(@RequestBody @Validated RegisterRequest registerRequest) {
        return userService.createUser(registerRequest).block();
    }

    @GetMapping("/users/{login}")
    @PreAuthorize("permitAll()")
    public UserReadDto findUserByLogin(@PathVariable String login) {
        return userService.findUserByLogin(login).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .block();
    }
}
