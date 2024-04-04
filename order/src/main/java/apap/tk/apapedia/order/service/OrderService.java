package apap.tk.apapedia.order.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import apap.tk.apapedia.order.dto.request.CreateOrderRequestDTO;
import apap.tk.apapedia.order.dto.response.GetOrderResponseDTO;
import apap.tk.apapedia.order.dto.response.GetOrderStatsResponseDTO;
import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.model.Order;
import apap.tk.apapedia.order.model.OrderItem;

public interface OrderService {
    Map<String, ?> createOrder(List<CreateOrderRequestDTO> order, Cart cart);

    void saveOrders(Map<Order, List<OrderItem>> orders, Cart cart, List<UUID> productIds);

    Optional<Order> getOrderById(UUID id);

    List<GetOrderResponseDTO> getOrderByCustomerId(UUID id);

    List<GetOrderResponseDTO> getOrderBySellerId(UUID id);

    void updateOrder(Order order, int status);

    List<GetOrderStatsResponseDTO> getOrderStatusCount(UUID sellerId);
    
}
