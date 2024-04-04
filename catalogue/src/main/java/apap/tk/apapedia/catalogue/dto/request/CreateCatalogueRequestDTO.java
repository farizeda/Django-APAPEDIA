package apap.tk.apapedia.catalogue.dto.request;


import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCatalogueRequestDTO {

    @NotNull
    private String name;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    private String description;

    @NotNull
    private Integer stock;

    @NotNull
    private String imageUrl;

    @NotNull
    private UUID categoryId;
  
}
