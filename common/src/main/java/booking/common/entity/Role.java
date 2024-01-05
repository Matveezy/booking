package booking.common.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    OWNER, ADMIN, USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
