package apap.tk.apapedia.frontend.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoriesRestDTO {
    private UUID id;
    private String name;
}
