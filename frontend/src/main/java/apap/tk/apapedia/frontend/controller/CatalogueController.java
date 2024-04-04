package apap.tk.apapedia.frontend.controller;

import apap.tk.apapedia.frontend.dto.CatalogueMapper;
import apap.tk.apapedia.frontend.dto.request.CreateCatalogueRequestDTO;
import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCatalogueDetailRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCategoriesRestDTO;
import apap.tk.apapedia.frontend.service.CatalogueService;
import apap.tk.apapedia.frontend.service.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/catalogue")
public class CatalogueController {

    final CatalogueService catalogueService;
    final CommonService commonService;

    final CatalogueMapper catalogueMapper;

    public CatalogueController(CatalogueService catalogueService, CommonService commonService, CatalogueMapper catalogueMapper) {
        this.catalogueService = catalogueService;
        this.commonService = commonService;
        this.catalogueMapper = catalogueMapper;
    }

    @GetMapping("")
    public String formAddCatalogue(Model model){
        List<GetCategoriesRestDTO> categories = catalogueService.getCategories();

        // Render into thymeleaf
        model.addAttribute("categories", categories);
        model.addAttribute("dto", new CreateCatalogueRequestDTO());
        model.addAttribute("user", commonService.getAuthenticatedUser());
        return "catalogue/create";
    }

    @PostMapping("")
    public String postCatalogue(
            @RequestParam("image") MultipartFile file,
            @ModelAttribute CreateCatalogueRequestDTO catalogueDTO,
            RedirectAttributes redirectAttrs
    ) {
        try {
            String imageUrl = catalogueService.uploadImage(file);
            if (imageUrl == null) {
                throw new IOException();
            }

            // Now send the DTO with the image path to the Catalogue microservice
            CommonResponseDTO<?> response = catalogueService.postCatalogue(catalogueDTO, imageUrl);

            if (!response.isSuccess()) {
                redirectAttrs.addFlashAttribute("error", response.getMessage());
                return "redirect:/";
            }

            redirectAttrs.addFlashAttribute("success", "Catalogue added!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Upload image failed!");
            return "redirect:/";
        }

    }

    @GetMapping("/{catalogueId}/update")
    public String formUpdateCatalogue(
            @PathVariable("catalogueId") UUID catalogueId,
            Model model
    ){
        List<GetCategoriesRestDTO> categories = catalogueService.getCategories();
        GetCatalogueDetailRestDTO catalogue = catalogueService.getCatalogue(catalogueId);

        CreateCatalogueRequestDTO dto = catalogueMapper.catalogueDetailToCreateCatalogue(catalogue);

        // Render into thymeleaf
        model.addAttribute("categories", categories);
        model.addAttribute("dto", dto);
        model.addAttribute("user", commonService.getAuthenticatedUser());
        return "catalogue/update";
    }

    @PostMapping("/{catalogueId}/update")
    public String updateCatalogue(
            @PathVariable("catalogueId") UUID catalogueId,
            @RequestParam("image") MultipartFile file,
            @ModelAttribute CreateCatalogueRequestDTO catalogueDTO,
            RedirectAttributes redirectAttrs
    ) {
        try {
            String imageUrl = !file.isEmpty() ? catalogueService.uploadImage(file) : catalogueDTO.getImageUrl();
            if (imageUrl == null) {
                throw new IOException();
            }

            // Now send the DTO with the image path to the Catalogue microservice
            CommonResponseDTO<?> response = catalogueService.updateCatalogue(catalogueDTO, imageUrl, catalogueId);

            if (!response.isSuccess()) {
                redirectAttrs.addFlashAttribute("error", response.getMessage());
                return "redirect:/";
            }

            redirectAttrs.addFlashAttribute("success", "Catalogue updated!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Upload image failed!");
            return "redirect:/";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCatalogue(
            @PathVariable("id") UUID id,
            RedirectAttributes redirectAttrs
    ){
        CommonResponseDTO<?> response = catalogueService.deleteCatalogue(id);

        if (!response.isSuccess()) {
            redirectAttrs.addFlashAttribute("error", response.getMessage());
            return "redirect:/";
        }

        redirectAttrs.addFlashAttribute("success", "Catalogue deleted!");
        return "redirect:/";
    }
}

    

