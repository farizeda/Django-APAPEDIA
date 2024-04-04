package apap.tk.apapedia.order.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class SecurityUserDetails implements UserDetails {
    private final String username;
    private final Set<GrantedAuthority> authorities;

    @Getter
    private final Claims jwtClaims;

    @Getter
    private final String token;

    public SecurityUserDetails(String username, Claims jwtClaims, Set<GrantedAuthority> authorities, String token) {
        this.username = username;
        this.jwtClaims = jwtClaims;
        this.authorities = authorities;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
