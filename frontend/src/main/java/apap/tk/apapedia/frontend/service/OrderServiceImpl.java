package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetOrderRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetOrderStatsRestDTO;
import apap.tk.apapedia.frontend.security.SecurityUserDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

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
    public List<GetOrderRestDTO> getOrders() {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        return Objects.requireNonNull(this.webClient.get()
                .uri("order/seller")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<List<GetOrderRestDTO>>>() {
                })
                .block()).getContent();
    }

    @Override
    public CommonResponseDTO<?> updateOrderStatus(UUID orderId, int status) {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();
        Map<String, Object> data = new HashMap<>();
        data.put("status", status);

        return this.webClient.put()
                .uri(String.format("order/%s", orderId))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(CommonResponseDTO.class)
                .block();
    }

    @Override
    public List<GetOrderStatsRestDTO> getOrderStats() {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        return Objects.requireNonNull(this.webClient.get()
                .uri("order/stats")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<List<GetOrderStatsRestDTO>>>() {
                })
                .block()).getContent();
    }
}
