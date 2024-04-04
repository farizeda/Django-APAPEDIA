package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCatalogueDetailRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCataloguesRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCategoriesRestDTO;
import apap.tk.apapedia.frontend.model.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import apap.tk.apapedia.frontend.dto.request.CreateCatalogueRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CatalogueService {
    List<GetCategoriesRestDTO> getCategories();
    String uploadImage(MultipartFile multipartFile) throws IOException;
    CommonResponseDTO<?> postCatalogue(CreateCatalogueRequestDTO catalogueDTO, String imageUrl);
    CommonResponseDTO<?> updateCatalogue(CreateCatalogueRequestDTO catalogueDTO, String imageUrl, UUID catalogueId);
    CommonResponseDTO<List<GetCataloguesRestDTO>> getCatalogues(User user);
    GetCatalogueDetailRestDTO getCatalogue(UUID catalogueId);
    CommonResponseDTO<?> deleteCatalogue(UUID id);
}
