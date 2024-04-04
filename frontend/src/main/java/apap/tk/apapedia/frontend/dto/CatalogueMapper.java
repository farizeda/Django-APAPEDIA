package apap.tk.apapedia.frontend.dto;

import apap.tk.apapedia.frontend.dto.request.CreateCatalogueRequestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCatalogueDetailRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatalogueMapper {
    @Mapping(target = "description", source = "productDescription")
    CreateCatalogueRequestDTO catalogueDetailToCreateCatalogue(GetCatalogueDetailRestDTO dto);
}
