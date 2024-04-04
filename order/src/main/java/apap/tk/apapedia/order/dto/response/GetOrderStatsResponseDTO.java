package apap.tk.apapedia.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderStatsResponseDTO {
    private String status;
    private int quantity;
}
