package booking.security.controller;

import booking.security.dto.AuthenticationRequest;
import booking.security.dto.AuthenticationResponse;
import booking.security.dto.RegisterRequest;
import booking.security.dto.ValidateTokenResponse;
import booking.security.entity.Role;
import booking.security.service.AuthenticationService;
import booking.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Validated RegisterRequest request) {
        Optional<AuthenticationResponse> responseOptional = authenticationService.register(request);
        return responseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Validated AuthenticationRequest request
    ) {
        Optional<AuthenticationResponse> responseOptional = authenticationService.authenticate(request);
        return responseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        if (username == null) return ResponseEntity.ok(ValidateTokenResponse.builder().isAuthenticated(false).build());
        List<Role> authorities = (List<Role>) httpServletRequest.getAttribute("authorities");
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        return ResponseEntity.ok(
                ValidateTokenResponse.builder()
                        .userId(userId)
                        .login(username)
                        .authorities(authorities)
                        .isAuthenticated(true)
                        .build()
        );
    }
}
