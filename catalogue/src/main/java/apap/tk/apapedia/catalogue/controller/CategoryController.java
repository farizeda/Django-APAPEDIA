package apap.tk.apapedia.catalogue.controller;

import apap.tk.apapedia.catalogue.dto.response.CommonResponseDTO;
import apap.tk.apapedia.catalogue.dto.response.GetCategoriesResponseDTO;
import apap.tk.apapedia.catalogue.model.Category;
import apap.tk.apapedia.catalogue.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {
    final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public ResponseEntity<CommonResponseDTO<List<GetCategoriesResponseDTO>>> getAllCategory(Model model){
        List<GetCategoriesResponseDTO> categoryList = categoryService.getAllCategory();
        CommonResponseDTO<List<GetCategoriesResponseDTO>> responseDTO = new CommonResponseDTO<>(
                true,
                "Category retrieved successfully",
                categoryList
        );

        return ResponseEntity.ok(responseDTO);
    }
}
