package apap.tk.apapedia.order.controller;

import apap.tk.apapedia.order.dto.CartItemMapper;
import apap.tk.apapedia.order.dto.request.CreateCartItemRequestDTO;
import apap.tk.apapedia.order.dto.request.CreateCartRequestDTO;
import apap.tk.apapedia.order.dto.request.UpdateCartItemRequestDTO;
import apap.tk.apapedia.order.dto.response.CommonResponseDTO;
import apap.tk.apapedia.order.dto.response.CreateCartResponseDTO;
import apap.tk.apapedia.order.dto.response.GetCartItemResponseDTO;
import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.model.CartItem;
import apap.tk.apapedia.order.service.CartItemService;
import apap.tk.apapedia.order.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;

    public CartController(CartService cartService, CartItemService cartItemService, CartItemMapper cartItemMapper) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.cartItemMapper = cartItemMapper;
    }

    @GetMapping(value = "/item")
    public ResponseEntity<CommonResponseDTO<List<GetCartItemResponseDTO>>> getCartItemCustomer() {
        Cart cart = cartService.getCart();
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User's cart not found!", null));
        }

        List<GetCartItemResponseDTO> response = new ArrayList<>();
        for (CartItem cartItem: cart.getCartItems()) {
            response.add(cartItemMapper.cartItemToGetCartItemResponseDTO(cartItem));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Cart items successfully created!", response));
    }

    @PostMapping("")
    public ResponseEntity<CommonResponseDTO<CreateCartResponseDTO>> createCart(
        @Valid @RequestBody CreateCartRequestDTO body
    ) {
        Cart cart = cartService.createCart(body.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDTO<>(true, "Cart successfully created!", new CreateCartResponseDTO(cart.getId())));
    }

    @PostMapping(value = "/item")
    public ResponseEntity<CommonResponseDTO<Null>> addCartItem(
            @Valid @RequestBody CreateCartItemRequestDTO body
    ) {
        Cart cart = cartService.getCart();
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User's cart not found!", null));
        }

        CartItem cartItem = cartItemMapper.createCartItemRequestDTOToCartItem(body);
        cartItemService.addToCart(cartItem, cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDTO<>(true, "Successfully added to cart!", null));
    }

    @PutMapping(value = "/item/{cartItemId}")
    public ResponseEntity<CommonResponseDTO<Null>> updateCartItem(@PathVariable("cartItemId") UUID cartItemId, @Valid @RequestBody UpdateCartItemRequestDTO body) {
        Cart cart = cartService.getCart();
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User's cart not found!", null));
        }

        Optional<CartItem> cartItem = cartItemService.getCartItem(cartItemId, cart);
        if (cartItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "Cart item didn't exists!", null));
        }

        cartItemService.updateCartItem(cartItem.get(), body.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Cart item updated successfully!", null));
    }

    @DeleteMapping(value = "/item/{cartItemId}")
    public ResponseEntity<CommonResponseDTO<Null>> deleteCartItem(@PathVariable("cartItemId") UUID cartItemId){
        Cart cart = cartService.getCart();
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "Cart didn't exists!", null));
        }

        Optional<CartItem> cartItem = cartItemService.getCartItem(cartItemId, cart);
        if (cartItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "Cart item didn't exists!", null));
        }

        cartItemService.deleteCartItems(cartItem.get());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Cart Item has been deleted", null));
    }
}
