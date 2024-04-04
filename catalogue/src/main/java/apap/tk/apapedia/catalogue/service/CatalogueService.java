package apap.tk.apapedia.catalogue.service;

import apap.tk.apapedia.catalogue.model.Catalogue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogueService {
    void createRestCatalogue(Catalogue catalogue);
    void deleteCatalogue(Catalogue catalogue);
    Optional<Catalogue> getCatalogueById(UUID id);
    Optional<Catalogue> getCatalogue(UUID id, UUID sellerId);
    List<Catalogue> getCataloguesBySellerId(UUID id);
    List<Catalogue> getAllCataloguesSorted();
    List<Catalogue> getAllCataloguesBy(String sortBy, String order);
    void updateRestCatalogue(Catalogue catalogue);
    List<Catalogue> getCatalogueByName(String name);
    List<Catalogue> getCatalogueByPriceRange(int minPrice, int maxPrice);
}
