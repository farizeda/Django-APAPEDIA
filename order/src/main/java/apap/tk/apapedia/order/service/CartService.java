package apap.tk.apapedia.order.service;

import java.util.UUID;

import apap.tk.apapedia.order.model.Cart;

public interface CartService {
    Cart createCart(UUID userId);
    Cart getCart();
    Cart getCartByCustomer(UUID id);
}
