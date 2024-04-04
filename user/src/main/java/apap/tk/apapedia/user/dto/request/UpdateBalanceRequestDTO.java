package apap.tk.apapedia.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequestDTO {
    @NotNull
    @Min(0)
    long amount;

    @NotNull
    String mutation;
}
