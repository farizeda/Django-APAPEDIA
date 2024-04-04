package apap.tk.apapedia.order.repository;

import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByProductId(UUID productId);
    Optional<CartItem> findByIdAndCart(UUID productId, Cart cart);
    void deleteByProductIdIn(List<UUID> productId);
}
