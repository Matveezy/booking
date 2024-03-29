package booking.hotel.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import java.util.List;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                withMockCustomUser.username(),
                withMockCustomUser.password(),
                List.of(withMockCustomUser.role()));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

}
