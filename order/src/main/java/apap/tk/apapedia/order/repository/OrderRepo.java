package apap.tk.apapedia.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.tk.apapedia.order.model.Order;

public interface OrderRepo extends JpaRepository<Order, UUID>{
    List<Order> findAllByCustomerId(UUID customerId);

    List<Order> findAllBySellerId(UUID sellerId);
}