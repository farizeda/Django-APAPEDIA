package apap.tk.apapedia.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SecurityFilter authfilter;

    public WebSecurityConfig(SecurityFilter authfilter) {
        this.authfilter = authfilter;
    }

    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception{
        http.securityMatcher("/user/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                    .anyRequest().authenticated()
            )
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(authfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
