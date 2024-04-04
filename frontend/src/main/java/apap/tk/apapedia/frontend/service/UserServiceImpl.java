package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.dto.UserMapper;
import apap.tk.apapedia.frontend.dto.request.RegistrationRequestDTO;
import apap.tk.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetUserRestDTO;
import apap.tk.apapedia.frontend.dto.rest.LoginRestDTO;
import apap.tk.apapedia.frontend.dto.rest.SSORestDTO;
import apap.tk.apapedia.frontend.security.SecurityToken;
import apap.tk.apapedia.frontend.security.SecurityUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Value("${service.user}")
    private String USER_SERVICE_URL;

    @Value("${service.client}")
    private String CLIENT_SERVICE_URL;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private WebClient webClient;

    @PostConstruct
    public void initialize() {
        this.webClient = WebClient.builder().baseUrl(this.USER_SERVICE_URL).build();
    }

    @Override
    public void register(RegistrationRequestDTO dto) {
        Map<String, String> data = new HashMap<>();
        data.put("username", dto.getUsername());
        data.put("password", dto.getPassword());
        data.put("email", dto.getEmail().toLowerCase());
        data.put("category", dto.getCategory());
        this.webClient.post().uri("/auth/registration").contentType(MediaType.APPLICATION_JSON).bodyValue(data).retrieve().bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<LoginRestDTO>>() {
        }).block();
    }

    @Override
    public CommonResponseDTO<?> login(String username) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        return this.webClient.post().uri("/auth/login/sso").contentType(MediaType.APPLICATION_JSON).bodyValue(data).retrieve().bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<LoginRestDTO>>() {
        }).block();

    }

    @Override
    public CommonResponseDTO<?> withdraw(long amount) {
        SecurityUserDetails user = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = user.getToken();
        UUID id = UUID.fromString(user.getJwtClaims().getSubject());

        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("mutation", "CREDIT");

        return this.webClient
                .put()
                .uri(String.format("/user/%s/balance", id))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(CommonResponseDTO.class)
                .block();
    }

    @Override
    public SSORestDTO validateTicket(String ticket) {
        WebClient SSOClient = WebClient.builder().baseUrl("https://sso.ui.ac.id/cas2").build();

        return SSOClient.get().uri(
                String.format(
                        "/serviceValidate?ticket=%s&service=%s",
                        ticket,
                        CLIENT_SERVICE_URL
                )
        ).retrieve().bodyToMono(SSORestDTO.class).block();
    }

    @Override
    public SecurityContext getSecurityContext(String token) {
        Claims payload = getPayload(token);

        SecurityUserDetails userDetails = getUserDetails(payload, token);
        SecurityToken authenticationToken = new SecurityToken(userDetails);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationToken);

        return securityContext;
    }

    public Claims getPayload(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private SecurityUserDetails getUserDetails(Claims payload, String token) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority((String) payload.get("role")));

        return new SecurityUserDetails((String) payload.get("userName"), payload, grantedAuthoritySet, token);
    }

    @Override
    public GetUserRestDTO getUserDetail() {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        return Objects.requireNonNull(webClient.get()
                        .uri("/user")
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<GetUserRestDTO>>() {
                        })
                        .block())
                .getContent();
    }

    @Override
    public CommonResponseDTO<?> updateUser(UpdateUserRequestDTO user) {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("address", user.getAddress());
        data.put("oldPassword", null);
        data.put("newPassword", null);

        return webClient.put()
                .uri("/user")
                .header("Authorization", "Bearer " + token)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(CommonResponseDTO.class)
                .block();
    }

    @Override
    public CommonResponseDTO<?> deleteUser() {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        return webClient.delete()
                .uri("/user")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(CommonResponseDTO.class)
                .block();
    }
}
