package apap.tk.apapedia.order.repository;

import apap.tk.apapedia.order.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepo extends JpaRepository<Cart, UUID>{
    Cart findFirstByUserId(UUID userId);
}
