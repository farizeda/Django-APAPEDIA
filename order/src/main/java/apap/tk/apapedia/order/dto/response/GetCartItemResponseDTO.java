package apap.tk.apapedia.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCartItemResponseDTO {
    private UUID id;
    private UUID productId;
    private Integer quantity;
    private Integer price;
    private UUID sellerId;
    private String productName;
}
