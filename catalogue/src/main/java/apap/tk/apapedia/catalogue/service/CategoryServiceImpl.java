package apap.tk.apapedia.catalogue.service;

import apap.tk.apapedia.catalogue.dto.response.GetCategoriesResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tk.apapedia.catalogue.model.Category;
import apap.tk.apapedia.catalogue.repository.CategoryRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public void createCategory(Category category) {
        categoryRepo.save(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + id));
    }

    public List<GetCategoriesResponseDTO> getAllCategory() {
        List<Category> categories = categoryRepo.findAll();
        List<GetCategoriesResponseDTO> allCategories = new ArrayList<>();
        for (Category category : categories) {
            GetCategoriesResponseDTO allCategory = new GetCategoriesResponseDTO();
            allCategory.setId(category.getId());
            allCategory.setName(category.getName());
            allCategories.add(allCategory);

        }
        return allCategories;
    }

    
}
