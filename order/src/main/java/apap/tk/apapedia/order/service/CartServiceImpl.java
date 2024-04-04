package apap.tk.apapedia.order.service;

import java.util.UUID;

import apap.tk.apapedia.order.repository.CartRepo;
import apap.tk.apapedia.order.security.SecurityUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import apap.tk.apapedia.order.model.Cart;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService{
    private final CartRepo cartRepo;

    public CartServiceImpl(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Override
    public Cart createCart(UUID userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepo.save(cart);
    }

    @Override
    public Cart getCart() {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return getCartByCustomer(UUID.fromString(authentication.getJwtClaims().getSubject()));
    }

    @Override
    public Cart getCartByCustomer(UUID userId) {
        return cartRepo.findFirstByUserId(userId);
    }
}
