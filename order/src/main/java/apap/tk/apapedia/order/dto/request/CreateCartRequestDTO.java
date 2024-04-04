package apap.tk.apapedia.order.dto.request;

import java.util.UUID;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CreateCartRequestDTO {
    @NotNull
    private UUID userId;
}
