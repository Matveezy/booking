package booking.user.security;

import booking.user.entity.User;
import booking.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final UserRepository userService;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        Optional<User> userOptional = Optional.of(userService.findByLogin(withMockCustomUser.username()));
        return userOptional.map(user -> {
            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                    user.getLogin(),
                    user.getPass(),
                    List.of(user.getRole())
            );
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);

            return securityContext;
        }).orElseThrow(EntityNotFoundException::new);
    }
}
