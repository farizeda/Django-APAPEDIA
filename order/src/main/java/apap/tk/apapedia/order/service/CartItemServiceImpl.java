package apap.tk.apapedia.order.service;

import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.model.CartItem;
import apap.tk.apapedia.order.repository.CartItemRepo;
import apap.tk.apapedia.order.repository.CartRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepo cartItemRepo;

    private final CartRepo cartRepo;

    public CartItemServiceImpl(CartItemRepo cartItemRepo, CartRepo cartRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
    }

    @Override
    public void addToCart(CartItem cartItem, Cart cart) {
        Optional<CartItem> existingCartItem = cartItemRepo.findByProductId(cartItem.getProductId());
        if (existingCartItem.isEmpty()) {
            List<CartItem> cartItems = cart.getCartItems();
            cartItems.add(cartItem);
            cart.setCartItems(cartItems);
            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getQuantity() * cartItem.getPrice());
            cartItem.setCart(cart);

            cartRepo.save(cart);
            cartItemRepo.save(cartItem);
        } else {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            cart.setTotalPrice(cart.getTotalPrice() + item.getQuantity() * item.getPrice());

            cartRepo.save(cart);
            cartItemRepo.save(item);
        }
    }

    @Override
    public void deleteCartItems(CartItem cartItem) {
        cartItemRepo.delete(cartItem);
    }


    @Override
    public Optional<CartItem> getCartItem(UUID id, Cart cart) {
        return cartItemRepo.findByIdAndCart(id, cart);
    }

    @Override
    public void updateCartItem(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);
    }
}
