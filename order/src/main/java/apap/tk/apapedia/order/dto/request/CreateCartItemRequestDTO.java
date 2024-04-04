package apap.tk.apapedia.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartItemRequestDTO {
    @NotNull
    private UUID productId;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    private UUID sellerId;

    @NotNull
    private String productName;
}
