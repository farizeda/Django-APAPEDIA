package apap.tk.apapedia.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import apap.tk.apapedia.order.dto.response.GetOrderResponseDTO;
import apap.tk.apapedia.order.dto.response.GetOrderStatsResponseDTO;
import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.repository.CartItemRepo;
import apap.tk.apapedia.order.repository.CartRepo;
import org.springframework.stereotype.Service;

import apap.tk.apapedia.order.dto.request.CreateOrderRequestDTO;
import apap.tk.apapedia.order.model.Order;
import apap.tk.apapedia.order.model.OrderItem;
import apap.tk.apapedia.order.repository.OrderRepo;
import apap.tk.apapedia.order.repository.OrderItemRepo;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final OrderItemRepo orderItemRepo;
    private final Map<Integer, String> statusName = Map.ofEntries(
            Map.entry(0, "Menunggu konfirmasi penjual"),
            Map.entry(1, "Dikonfirmasi  penjual"),
            Map.entry(2, "Menunggu kurir"),
            Map.entry(3, "Dalam perjalanan"),
            Map.entry(4, "Barang diterima"),
            Map.entry(5, "Selesai")
    );

    public OrderServiceImpl(OrderRepo orderRepo, CartRepo cartRepo, CartItemRepo cartItemRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Override
    public Map<String, ?> createOrder(List<CreateOrderRequestDTO> order, Cart cart) {
        Map<Order, List<OrderItem>> orderCollection = new HashMap<>();
        int totalPrice = 0;
        List<UUID> productIds = new ArrayList<>();

        Map<UUID, Order> orderProduct = new HashMap<>();

        for(CreateOrderRequestDTO orderItem : order) {
            OrderItem item = new OrderItem();
            item.setQuantity(orderItem.getQuantity());
            item.setProductId(orderItem.getProduct().getId());
            item.setProductName(orderItem.getProduct().getName());
            item.setProductPrice(orderItem.getProduct().getPrice());

            UUID sellerId = orderItem.getProduct().getSellerId();

            if(orderProduct.containsKey(sellerId)) {
                Order existingOrder = orderProduct.get(sellerId);

                int price = item.getProductPrice() * item.getQuantity();
                existingOrder.setTotalPrice(existingOrder.getTotalPrice() + price);
                totalPrice += price;

                item.setOrder(existingOrder);
                orderCollection.get(existingOrder).add(item);
            } else {
                Order orderToSave = new Order();
                orderToSave.setId(UUID.randomUUID());
                orderToSave.setSellerId(sellerId);
                orderToSave.setCustomerId(cart.getUserId());
                item.setOrder(orderToSave);

                int price = item.getProductPrice() * item.getQuantity();
                orderToSave.setTotalPrice(price);
                totalPrice += price;

                orderCollection.put(orderToSave, new ArrayList<>());
                orderCollection.get(orderToSave).add(item);
                orderProduct.put(sellerId, orderToSave);
            }

            productIds.add(orderItem.getProduct().getId());
        }

        return Map.ofEntries(
                Map.entry("order", orderCollection),
                Map.entry("productIds", productIds),
                Map.entry("totalPrice", totalPrice)
        );
    }

    @Override
    public void saveOrders(Map<Order, List<OrderItem>> orders, Cart cart, List<UUID> productIds) {
        for (Map.Entry<Order, List<OrderItem>> order: orders.entrySet()) {
            orderRepo.save(order.getKey());
            orderItemRepo.saveAll(order.getValue());
        }

        cartItemRepo.deleteByProductIdIn(productIds);

        cart.setTotalPrice(0);
        cartRepo.save(cart);
    }

    @Override
    public Optional<Order> getOrderById(UUID id) {
        return orderRepo.findById(id);
    }

    @Override
    public List<GetOrderResponseDTO> getOrderByCustomerId(UUID id) {
        return getOrders(orderRepo.findAllByCustomerId(id));
    }

    @Override
    public List<GetOrderResponseDTO> getOrderBySellerId(UUID id) {
        return getOrders(orderRepo.findAllBySellerId(id));
    }

    private List<GetOrderResponseDTO> getOrders(List<Order> orders) {
        List<GetOrderResponseDTO> orderResults = new ArrayList<>();

        for (Order order: orders) {
            List<GetOrderResponseDTO.Item> orderItemResults = new ArrayList<>();

            for (OrderItem orderItem: order.getOrderItems()) {
                GetOrderResponseDTO.Item orderItemResult = GetOrderResponseDTO.Item.builder()
                        .id(orderItem.getId())
                        .productId(orderItem.getProductId())
                        .name(orderItem.getProductName())
                        .price(orderItem.getProductPrice())
                        .quantity(orderItem.getQuantity())
                        .build();

                orderItemResults.add(orderItemResult);
            }

            GetOrderResponseDTO orderResult = GetOrderResponseDTO.builder()
                    .id(order.getId())
                    .totalPrice(order.getTotalPrice())
                    .seller(order.getSellerId())
                    .status(order.getStatus())
                    .statusName(statusName.get(order.getStatus()))
                    .items(orderItemResults)
                    .build();

            orderResults.add(orderResult);
        }

        return orderResults;
    }

    @Override
    public void updateOrder(Order order, int status) {
        order.setStatus(status);
        orderRepo.save(order);
    }

    @Override
    public List<GetOrderStatsResponseDTO> getOrderStatusCount(UUID sellerId) {
        Map<String, Integer> orderStatusCounts = new HashMap<>(Map.ofEntries(
                Map.entry("Menunggu konfirmasi penjual", 0),
                Map.entry("Dikonfirmasi  penjual", 0),
                Map.entry("Menunggu kurir", 0),
                Map.entry("Dalam perjalanan", 0),
                Map.entry("Barang diterima", 0)
        ));

        List<Order> listOrder = orderRepo.findAllBySellerId(sellerId);

        for(Order order : listOrder) {
            int statusCode = order.getStatus();
            if (statusCode != 5) {
                String status = statusName.get(statusCode);
                orderStatusCounts.put(status, orderStatusCounts.get(status) + 1);
            }
        }

        List<GetOrderStatsResponseDTO> orderStats = new ArrayList<>();
        for (Map.Entry<String, Integer> orderStatusCount: orderStatusCounts.entrySet()) {
            GetOrderStatsResponseDTO orderStat = new GetOrderStatsResponseDTO();
            orderStat.setStatus(orderStatusCount.getKey());
            orderStat.setQuantity(orderStatusCount.getValue());
            orderStats.add(orderStat);
        }

        return orderStats;
    }
}
