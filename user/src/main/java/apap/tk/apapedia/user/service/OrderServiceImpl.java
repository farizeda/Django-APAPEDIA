package apap.tk.apapedia.user.service;

import apap.tk.apapedia.user.dto.response.CommonResponseDTO;
import apap.tk.apapedia.user.dto.rest.CreateCartRestDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("${service.order}")
    private String ORDER_SERVICE_URL;

    private WebClient webClient;

    @PostConstruct
    public void initialize() {
        this.webClient = WebClient.builder().baseUrl(this.ORDER_SERVICE_URL).build();
    }

    @Override
    public CommonResponseDTO<CreateCartRestDTO> populateCartId(UUID userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);

        return this.webClient.post().uri("/cart").contentType(MediaType.APPLICATION_JSON).bodyValue(data).retrieve().bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<CreateCartRestDTO>>() {}).block();
    }
}
