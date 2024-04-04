package apap.tk.apapedia.order.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SecurityToken extends AbstractAuthenticationToken {
    private final SecurityUserDetails userDetails;

    public SecurityToken(SecurityUserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
