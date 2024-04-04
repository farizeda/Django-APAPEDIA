package apap.tk.apapedia.frontend.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCatalogueDetailRestDTO extends GetCataloguesRestDTO {
    private String productDescription;
    private UUID categoryId;
}
