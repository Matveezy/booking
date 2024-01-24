package booking.user.controller;

import booking.user.dto.*;
import booking.user.service.UserService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler({CallNotPermittedException.class})
    public ResponseEntity<?> handleExternalServiceExceptions() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("External service is unavailable now.");
    }

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<?> handleUnexpectedServiceExceptions() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Couldn't make call for external service.");
    }
}

