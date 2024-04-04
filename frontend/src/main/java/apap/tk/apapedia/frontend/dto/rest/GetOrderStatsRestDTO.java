package apap.tk.apapedia.frontend.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderStatsRestDTO {
    private String status;
    private int quantity;
}
