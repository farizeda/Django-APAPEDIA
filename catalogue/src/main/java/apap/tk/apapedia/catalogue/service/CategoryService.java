package apap.tk.apapedia.catalogue.service;

import apap.tk.apapedia.catalogue.dto.response.GetCategoriesResponseDTO;
import apap.tk.apapedia.catalogue.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    void createCategory(Category category);
    Category getCategoryById(UUID id);

    List<GetCategoriesResponseDTO> getAllCategory();
}
