package apap.tk.apapedia.catalogue.controller;

import apap.tk.apapedia.catalogue.dto.CatalogueMapper;
import apap.tk.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import apap.tk.apapedia.catalogue.dto.response.GetCatalogueDetailResponseDTO;
import apap.tk.apapedia.catalogue.model.Catalogue;
import apap.tk.apapedia.catalogue.model.Category;
import apap.tk.apapedia.catalogue.security.SecurityUserDetails;
import apap.tk.apapedia.catalogue.service.CatalogueService;
import apap.tk.apapedia.catalogue.service.CategoryService;
import apap.tk.apapedia.catalogue.dto.response.CommonResponseDTO;
import apap.tk.apapedia.catalogue.dto.response.ReadCatalogueResponseDTO;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.validation.constraints.Null;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {

    private final CatalogueService catalogueService;

    private final CatalogueMapper catalogueMapper;

    private final CategoryService categoryService;

    public CatalogueController(CatalogueService catalogueService, CatalogueMapper catalogueMapper, CategoryService categoryService) {
        this.catalogueService = catalogueService;
        this.catalogueMapper = catalogueMapper;
        this.categoryService = categoryService;
    }

    // Catalogue Service 1
    @PostMapping("")
    public ResponseEntity<CommonResponseDTO<Null>> addCatalogue(
            @Valid @RequestBody CreateCatalogueRequestDTO catalogueDTO,
            BindingResult bindingResult
    ) {
        // Binding Error
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new CommonResponseDTO<>(false, errorMessage, null));
        }

        try {
            // Map Dto to Catalogue
            Catalogue catalogue = catalogueMapper.catalogueDTOToCatalogue(catalogueDTO);

            SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Set SellerId
            catalogue.setSellerId(UUID.fromString(authentication.getJwtClaims().getSubject()));

            // Set Category 
            var category = categoryService.getCategoryById(catalogueDTO.getCategoryId());
            catalogue.setCategory(category);

            // Saves Catalogue
            catalogueService.createRestCatalogue(catalogue);

            // Success Response 
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDTO<>(true, "Catalogue successfully created", null));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CommonResponseDTO<>(false, "Catalogue with the same identifier already exists", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDTO<>(false, "An error occurred while creating the catalogue", null));
        }
    }

    // Catalogue Service 2
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<CommonResponseDTO<List<ReadCatalogueResponseDTO>>> getCataloguesBySellerId(
            @PathVariable("sellerId") UUID sellerId
    ) {
        try {
            List<Catalogue> catalogues = catalogueService.getCataloguesBySellerId(sellerId);
    
            // If no catalogues are found
            if (catalogues.isEmpty()) {
                return ResponseEntity.ok(new CommonResponseDTO<>(false, "No catalogues found for the seller", null));
            }

            List<ReadCatalogueResponseDTO> cataloguesDTO = new ArrayList<>();
            for (Catalogue catalogue: catalogues) {
                cataloguesDTO.add(catalogueMapper.cataloguesToReadCatalogueResponseDTOs(catalogue));
            }
            
            return ResponseEntity.ok(new CommonResponseDTO<>(true, "Catalogues successfully retrieved", cataloguesDTO));

        } catch (Exception e) {
            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseDTO<>(false, "An error occurred while retrieving the catalogues", null));
        }
    }
    
    // Catalogue Service 3
    @GetMapping("")
    public ResponseEntity<CommonResponseDTO<List<ReadCatalogueResponseDTO>>> getCataloguesAsc(){
        try {
            List<Catalogue> catalogues = catalogueService.getAllCataloguesSorted();
    
            // If no catalogues are found
            if (catalogues.isEmpty()) {
                return ResponseEntity.ok(new CommonResponseDTO<>(false, "No catalogues found", null));
            }
            
            List<ReadCatalogueResponseDTO> cataloguesDTO = new ArrayList<>();
            for (Catalogue catalogue: catalogues) {
                cataloguesDTO.add(catalogueMapper.cataloguesToReadCatalogueResponseDTOs(catalogue));
            }
            
            return ResponseEntity.ok(new CommonResponseDTO<>(true, "Catalogues successfully retrieved", cataloguesDTO));

        } catch (Exception e) {
            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseDTO<>(false, "An error occurred while retrieving the catalogues", null));
        }
    }

    // Catalogue Service 4
    @GetMapping("/{catalogueId}")
    public ResponseEntity<CommonResponseDTO<GetCatalogueDetailResponseDTO>> getCatalogueByCatalogueId(
            @PathVariable("catalogueId") UUID catalogueId
    ){
        try {
            // Retrieve Catalogue by Id
            Optional<Catalogue> catalogue = catalogueService.getCatalogueById(catalogueId);
            if (catalogue.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(true, "Catalogue not found!", null));
            }

            // Map catalogue to DTO
            GetCatalogueDetailResponseDTO responseDTO = catalogueMapper.catalogueToGetCatalogueDetailResponseDTO(catalogue.get());

            // Return response
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Catalogue successfully retrieved", responseDTO));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new CommonResponseDTO<>(false, "An error occurred while retrieving the catalogue", null));
        }
     
    }


    @DeleteMapping("/{catalogueId}")
    public ResponseEntity<CommonResponseDTO<Null>> deleteCatalogue(
            @PathVariable(value = "catalogueId", required = false) UUID catalogueId
    ){
        try {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Set SellerId
        UUID seller = UUID.fromString(authentication.getJwtClaims().getSubject());

        Optional<Catalogue> catalogue = catalogueService.getCatalogue(catalogueId, seller);

        if (catalogue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponseDTO<>(false, "Catalogue not found!", null));
        }

        catalogueService.deleteCatalogue(catalogue.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDTO<>(true, "Catalogue successfully deleted", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDTO<>(false, "An error occurred while deleting the catalogue", null));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponseDTO<Null>> updateCatalogue(
            @PathVariable(value = "id", required = false) UUID catalogueId,
            @Valid @RequestBody CreateCatalogueRequestDTO body
    ) {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Set SellerId
        UUID seller = UUID.fromString(authentication.getJwtClaims().getSubject());

        Optional<Catalogue> catalogue = catalogueService.getCatalogue(catalogueId, seller);

        if (catalogue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponseDTO<>(false, "Catalogue not found!", null));
        }

        Catalogue existingCatalogue = catalogue.get();

        // Update catalog properties
        existingCatalogue.setProductName(body.getName());
        existingCatalogue.setPrice(body.getPrice());
        existingCatalogue.setProductDescription(body.getDescription());
        existingCatalogue.setStock(body.getStock());
        existingCatalogue.setImage(body.getImageUrl());

        Category category = categoryService.getCategoryById(body.getCategoryId());
        existingCatalogue.setCategory(category);

        // Update any other catalog-specific logic here

        catalogueService.updateRestCatalogue(existingCatalogue);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponseDTO<>(true, "Catalogue updated successfully!", null));


    }

    @GetMapping(value = "", params = { "name" })
    public ResponseEntity<CommonResponseDTO<List<ReadCatalogueResponseDTO>>> getCatalogueByName(
            @RequestParam("name") String name
    ) {
        List<Catalogue> catalogueListByName = catalogueService.getCatalogueByName(name);

        if (catalogueListByName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponseDTO<>(false, "No catalogue found with the specified name", null));
        }

        List<ReadCatalogueResponseDTO> cataloguesDTO = new ArrayList<>();
        for (Catalogue catalogue: catalogueListByName) {
            cataloguesDTO.add(catalogueMapper.cataloguesToReadCatalogueResponseDTOs(catalogue));
        }

        return ResponseEntity.ok(new CommonResponseDTO<>(true, "Catalogue retrieved successfully", cataloguesDTO));
    }

    @GetMapping(value = "", params = { "minPrice", "maxPrice" })
    public ResponseEntity<CommonResponseDTO<List<ReadCatalogueResponseDTO>>> getCatalogueByPriceRange(
            @RequestParam("minPrice") int minPrice,
            @RequestParam("maxPrice") int maxPrice
    ) {
        List<Catalogue> catalogListByPrice = catalogueService.getCatalogueByPriceRange(minPrice, maxPrice);

        if (catalogListByPrice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponseDTO<>(false, "No catalogue found within the specified price range", null));
        }

        List<ReadCatalogueResponseDTO> cataloguesDTO = new ArrayList<>();
        for (Catalogue catalogue: catalogListByPrice) {
            cataloguesDTO.add(catalogueMapper.cataloguesToReadCatalogueResponseDTOs(catalogue));
        }

        return ResponseEntity.ok(new CommonResponseDTO<>(true, "Catalogue retrieved successfully", cataloguesDTO));
    }

    @GetMapping(value = "", params = { "sortBy", "order" })
    public ResponseEntity<CommonResponseDTO<List<ReadCatalogueResponseDTO>>> getSortCatalogue(
            @RequestParam(value = "sortBy", defaultValue = "product_name") String sortBy,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ) {
        List<Catalogue> catalogueListByName = catalogueService.getAllCataloguesBy(sortBy, order);

        if (catalogueListByName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponseDTO<>(false, "No catalogue found with the specified name", null));
        }

        List<ReadCatalogueResponseDTO> cataloguesDTO = new ArrayList<>();
        for (Catalogue catalogue: catalogueListByName) {
            cataloguesDTO.add(catalogueMapper.cataloguesToReadCatalogueResponseDTOs(catalogue));
        }

        return ResponseEntity.ok(new CommonResponseDTO<>(true, "Catalogue retrieved successfully", cataloguesDTO));
    }
}



