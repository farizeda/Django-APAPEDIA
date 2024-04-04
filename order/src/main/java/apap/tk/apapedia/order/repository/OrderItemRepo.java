package apap.tk.apapedia.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.tk.apapedia.order.model.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, UUID> {
    
}
