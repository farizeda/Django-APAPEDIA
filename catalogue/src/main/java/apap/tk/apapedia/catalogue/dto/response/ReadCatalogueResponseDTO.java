package apap.tk.apapedia.catalogue.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadCatalogueResponseDTO {
    private UUID id;
    private String imageUrl;
    private String name;
    private Integer price;
    private String categoryName;
    private Integer stock;
    private UUID seller;
}
