package apap.tk.apapedia.order.controller;

import apap.tk.apapedia.order.dto.response.GetOrderResponseDTO;
import apap.tk.apapedia.order.dto.response.GetOrderStatsResponseDTO;
import apap.tk.apapedia.order.dto.rest.GetUserRestDTO;
import apap.tk.apapedia.order.model.Cart;
import apap.tk.apapedia.order.model.OrderItem;
import apap.tk.apapedia.order.security.SecurityUserDetails;
import apap.tk.apapedia.order.service.CartService;
import apap.tk.apapedia.order.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.tk.apapedia.order.dto.request.CreateOrderRequestDTO;
import apap.tk.apapedia.order.dto.request.UpdateOrderRequestDTO;
import apap.tk.apapedia.order.dto.response.CommonResponseDTO;
import apap.tk.apapedia.order.model.Order;
import apap.tk.apapedia.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("")
    public  ResponseEntity<CommonResponseDTO<Null>> addOrder(@Valid @RequestBody List<CreateOrderRequestDTO> body, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDTO<>(false, "Invalid input!", null));
        }

        Cart cart = cartService.getCart();

        CommonResponseDTO<GetUserRestDTO> getUserResponse = userService.getUser();
        if (!getUserResponse.isSuccess() || getUserResponse.getContent() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDTO<>(false, "Oops! Something went wrong", null));
        }

        Map<String, ?> result = orderService.createOrder(body, cart);

        @SuppressWarnings("unchecked")
        Map<Order, List<OrderItem>> orders = (Map<Order, List<OrderItem>>) result.get("order");

        int totalPrice = (int) result.get("totalPrice");
        int balance = getUserResponse.getContent().getBalance();
        if (balance < totalPrice) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CommonResponseDTO<>(false, "Insufficient balance!", null));
        }

        @SuppressWarnings("unchecked")
        List<UUID> productIds = (List<UUID>) result.get("productIds");

        orderService.saveOrders(orders, cart, productIds);

        CommonResponseDTO<?> updateBalance = userService.updateBalance(totalPrice, "CREDIT", cart.getUserId());
        if (!updateBalance.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDTO<>(false, "Oops! Something went wrong", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDTO<>(true, "Order successfully created!", null));
    }

    @GetMapping(value = "/customer")
    public ResponseEntity<CommonResponseDTO<List<GetOrderResponseDTO>>> getOrderCustomer() {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<GetOrderResponseDTO> response = orderService.getOrderByCustomerId(UUID.fromString(authentication.getJwtClaims().getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Order successfully retrieved!", response));
    }

    @GetMapping(value = "/seller")
    public ResponseEntity<CommonResponseDTO<List<GetOrderResponseDTO>>> getOrderSeller() {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<GetOrderResponseDTO> response = orderService.getOrderBySellerId(UUID.fromString(authentication.getJwtClaims().getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Order successfully retrieved!", response));
    }

     @PutMapping(value = "/{id}")
     public ResponseEntity<CommonResponseDTO<Null>> updateOrder(@PathVariable("id") UUID id, @Valid @RequestBody UpdateOrderRequestDTO body, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDTO<>(false, "Invalid input!", null));
        }

        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = (String) authentication.getJwtClaims().get("role");

        if(role.equals("Customer") && body.getStatus() < 4 || role.equals("Seller") && body.getStatus() > 3){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CommonResponseDTO<>(false, "Unauthorized Access!", null));
        }

        Optional<Order> order = orderService.getOrderById(id);
        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "Order not found!", null));
        }

        Order existingOrder = order.get();
        int existingOrderStatus = existingOrder.getStatus();
        int status = body.getStatus();

        if (existingOrderStatus < 3 && status >= 4 || existingOrderStatus >= 4 && status <= 3 || existingOrderStatus == 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDTO<>(false, "Order status invalid!", null));
        }

        if (status == 5) {
            CommonResponseDTO<?> userServiceResponse = userService.updateBalance(existingOrder.getTotalPrice(), "DEBIT", existingOrder.getSellerId());
            if (!userServiceResponse.isSuccess()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDTO<>(false, "Oops! Something went wrong", null));
            }
        }

        orderService.updateOrder(existingOrder, status);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Order has been updated", null));
     }

    @GetMapping(value = "/stats")
    public ResponseEntity<CommonResponseDTO<List<GetOrderStatsResponseDTO>>> getOrderStatusCount() {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<GetOrderStatsResponseDTO> response = orderService.getOrderStatusCount(UUID.fromString(authentication.getJwtClaims().getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Order stats successfully retrieved!", response));
    }
}
