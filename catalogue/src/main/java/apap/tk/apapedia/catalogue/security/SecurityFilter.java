package apap.tk.apapedia.catalogue.security;

import apap.tk.apapedia.catalogue.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request, @NonNull  HttpServletResponse response, @NonNull  FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (token != null) {
                Claims payload = jwtService.getPayload(token);

                SecurityUserDetails userDetails = getUserDetails(payload, token);
                SecurityToken authenticationToken = new SecurityToken(userDetails);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request,response);
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private SecurityUserDetails getUserDetails(Claims payload, String token) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority((String) payload.get("role")));

        return new SecurityUserDetails((String) payload.get("userName"), payload, grantedAuthoritySet, token);
    }
}
