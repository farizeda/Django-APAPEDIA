package apap.tk.apapedia.order.dto;

import apap.tk.apapedia.order.dto.response.GetCartItemResponseDTO;
import org.mapstruct.Mapper;
import apap.tk.apapedia.order.dto.request.CreateCartItemRequestDTO;

import apap.tk.apapedia.order.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem createCartItemRequestDTOToCartItem(CreateCartItemRequestDTO createCartItemRequestDTO);
    GetCartItemResponseDTO cartItemToGetCartItemResponseDTO(CartItem cartItem);
}
