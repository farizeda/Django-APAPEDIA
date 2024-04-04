package apap.tk.apapedia.order.service;

import apap.tk.apapedia.order.dto.response.CommonResponseDTO;
import apap.tk.apapedia.order.dto.rest.GetUserRestDTO;
import apap.tk.apapedia.order.security.SecurityUserDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Value("${service.user}")
    private String USER_SERVICE_URL;

    private WebClient webClient;

    @PostConstruct
    public void initialize() {
        this.webClient = WebClient.builder().baseUrl(this.USER_SERVICE_URL).build();
    }

    @Override
    public CommonResponseDTO<?> updateBalance(int amount, String mutation, UUID userId) {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("mutation", mutation);

        return this.webClient.put().uri(String.format("/user/%s/balance", userId))
                .header("Authorization", "Bearer " + authentication.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data).retrieve().bodyToMono(CommonResponseDTO.class).block();
    }

    @Override
    public CommonResponseDTO<GetUserRestDTO> getUser() {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.webClient.get().uri("/user")
                .header("Authorization", "Bearer " + authentication.getToken())
                .retrieve().bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<GetUserRestDTO>>() {}).block();
    }
}
