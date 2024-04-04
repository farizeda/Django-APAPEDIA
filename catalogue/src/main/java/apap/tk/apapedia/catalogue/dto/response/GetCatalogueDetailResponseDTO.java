package apap.tk.apapedia.catalogue.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCatalogueDetailResponseDTO extends ReadCatalogueResponseDTO{
    private String productDescription;
    private UUID categoryId;
}
