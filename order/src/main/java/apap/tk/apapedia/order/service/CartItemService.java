package apap.tk.apapedia.order.service;

import java.util.Optional;
import java.util.UUID;

import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.model.CartItem;

public interface CartItemService {

    void addToCart(CartItem cartItem, Cart cart);
    void updateCartItem(CartItem cartItem, int quantity);
    Optional<CartItem> getCartItem(UUID id, Cart cart);
    void deleteCartItems(CartItem cartItem);

}
