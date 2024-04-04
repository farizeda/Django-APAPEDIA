package apap.tk.apapedia.catalogue.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoriesResponseDTO {
    private UUID id;
    private String name;
}
