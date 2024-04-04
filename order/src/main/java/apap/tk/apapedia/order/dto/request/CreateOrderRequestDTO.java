package apap.tk.apapedia.order.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequestDTO {
    @NotNull
    @Min(0)
    private Integer quantity;
    private Product product;
    

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Product{
        @NotNull
        private UUID id;

        @NotNull
        private String name;

        @NotNull
        @Min(0)
        private Integer price;

        @NotNull
        private UUID sellerId;
    }
}
