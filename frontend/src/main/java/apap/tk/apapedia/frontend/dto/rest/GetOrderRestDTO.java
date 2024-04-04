package apap.tk.apapedia.frontend.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderRestDTO {
    private UUID id;
    private int status;
    private String statusName;
    private int totalPrice;
    private UUID seller;
    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private UUID id;
        private UUID productId;
        private String name;
        private int price;
        private int quantity;
    }
}
