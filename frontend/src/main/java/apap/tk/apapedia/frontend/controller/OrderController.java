package apap.tk.apapedia.frontend.controller;

import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetOrderRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetOrderStatsRestDTO;
import apap.tk.apapedia.frontend.service.CommonService;
import apap.tk.apapedia.frontend.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;
    private final CommonService commonService;

    private final Map<Integer, String> actionName = Map.ofEntries(
            Map.entry(0, "Confirm"),
            Map.entry(1, "Order Courier"),
            Map.entry(2, "On Process")
    );

    public OrderController(OrderService orderService, CommonService commonService) {
        this.orderService = orderService;
        this.commonService = commonService;
    }

    @GetMapping("stats")
    public String getStats(Model model) {
        List<GetOrderStatsRestDTO> orderStats = orderService.getOrderStats();
        model.addAttribute("orderStats", orderStats);
        model.addAttribute("user", commonService.getAuthenticatedUser());
        return "order/stats";
    }

    @GetMapping("")
    public String getOrder(Model model) {
        List<GetOrderRestDTO> orders = orderService.getOrders();

        model.addAttribute("orders", orders);
        model.addAttribute("user", commonService.getAuthenticatedUser());
        model.addAttribute("actions", actionName);
        return "order/index";
    }

    @GetMapping("/{id}/update")
    public String updateOrderStatus(
            @PathVariable("id") UUID orderId,
            @RequestParam(value = "nextStatus") int nextStatus,
            RedirectAttributes redirectAttrs
    ) {
        CommonResponseDTO<?> response = orderService.updateOrderStatus(orderId, nextStatus);
        if (!response.isSuccess()) {
            redirectAttrs.addFlashAttribute("error", "Withdraw failed!");
            return "redirect:/order";
        }

        redirectAttrs.addFlashAttribute("success", "Status updated!");
        return "redirect:/order";
    }
}
