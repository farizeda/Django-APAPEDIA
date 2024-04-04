package apap.tk.apapedia.catalogue.dto;

import apap.tk.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import apap.tk.apapedia.catalogue.dto.response.GetCatalogueDetailResponseDTO;
import apap.tk.apapedia.catalogue.dto.response.ReadCatalogueResponseDTO;
import apap.tk.apapedia.catalogue.model.Catalogue;
import apap.tk.apapedia.catalogue.service.CategoryService;

import java.util.List;

import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {CategoryService.class})
public interface CatalogueMapper {

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true) // Implementasi di model
    @Mapping(target = "sellerId", ignore = true) // Implementasi di controller
    @Mapping(target = "category", ignore = true) // Implementasi di controller
    @Mapping(target = "productName", source = "name") // Map description from DTO to productDescription in entity
    @Mapping(target = "productDescription", source = "description") // Map description from DTO to productDescription in entity
    @Mapping(target = "image", source = "imageUrl") // Map imageUrl from DTO to image in entity
    Catalogue catalogueDTOToCatalogue(CreateCatalogueRequestDTO dto);


    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "seller", source = "sellerId")
    @Mapping(target = "categoryId", expression = "java(catalogue.getCategory() != null ? catalogue.getCategory().getId() : null)")
    @Mapping(target = "categoryName", expression = "java(catalogue.getCategory() != null ? catalogue.getCategory().getName() : null)")
    GetCatalogueDetailResponseDTO catalogueToGetCatalogueDetailResponseDTO(Catalogue catalogue);

    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "seller", source = "sellerId")
    @Mapping(target = "categoryName", expression = "java(catalogue.getCategory() != null ? catalogue.getCategory().getName() : null)")
    ReadCatalogueResponseDTO cataloguesToReadCatalogueResponseDTOs(Catalogue catalogue);
}
