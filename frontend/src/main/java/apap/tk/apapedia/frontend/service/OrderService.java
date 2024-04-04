package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetOrderRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetOrderStatsRestDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<GetOrderRestDTO> getOrders();
    CommonResponseDTO<?> updateOrderStatus(UUID orderId, int status);
    List<GetOrderStatsRestDTO> getOrderStats();
}
