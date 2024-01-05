package booking.common.controller;

import booking.common.dto.AuthenticationRequest;
import booking.common.dto.AuthenticationResponse;
import booking.common.dto.RegisterRequest;
import booking.common.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Validated RegisterRequest request) {
        Optional<AuthenticationResponse> responseOptional = authenticationService.register(request);
        return responseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/authenticate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Validated AuthenticationRequest request
    ) {
        Optional<AuthenticationResponse> responseOptional = authenticationService.authenticate(request);
        return responseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}