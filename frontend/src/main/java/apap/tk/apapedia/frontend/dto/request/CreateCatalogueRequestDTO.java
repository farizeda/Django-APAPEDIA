package apap.tk.apapedia.frontend.dto.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCatalogueRequestDTO {
    private UUID id;

    private String imageUrl;

    @NotNull
    private String name;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    @JsonProperty("productDescription")
    private String description;

    @NotNull
    private Integer stock;

    @NotNull
    private UUID categoryId;
  
}
