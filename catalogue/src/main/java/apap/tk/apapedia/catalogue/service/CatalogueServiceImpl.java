package apap.tk.apapedia.catalogue.service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import apap.tk.apapedia.catalogue.model.Catalogue;
import apap.tk.apapedia.catalogue.repository.CatalogueRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class CatalogueServiceImpl implements CatalogueService{
    
    private final CatalogueRepo catalogueRepo;

    public CatalogueServiceImpl(CatalogueRepo catalogueRepo) {
        this.catalogueRepo = catalogueRepo;
    }

    @Override
    public void createRestCatalogue(Catalogue catalogue) {
        catalogueRepo.save(catalogue);
    }

    @Override
    public void deleteCatalogue(Catalogue catalogue) {
        catalogueRepo.delete(catalogue);
    }

    @Override
    public Optional<Catalogue> getCatalogueById(UUID id) {
        return catalogueRepo.findById(id);
    }

    @Override
    public Optional<Catalogue> getCatalogue(UUID id, UUID seller) {
        return catalogueRepo.findCatalogByIdAndSellerId(id, seller);
    }

    @Override
    public List<Catalogue> getCataloguesBySellerId(UUID id) {
        return catalogueRepo.findBySellerId(id);
    }

    @Override
    public List<Catalogue> getAllCataloguesSorted() {
        return catalogueRepo.findAllByOrderByIgnoreCase(Sort.by(Sort.Direction.ASC, "productName"));
    }

    @Override
    public List<Catalogue> getAllCataloguesBy(String sortBy, String order) {
        return catalogueRepo.findAllByOrderByIgnoreCase(Sort.by(Sort.Direction.fromString(order), sortBy));
    }

    @Override
    public void updateRestCatalogue(Catalogue catalogue){
        catalogueRepo.save(catalogue);
    }

    @Override
    public List<Catalogue> getCatalogueByName(String name) {
        return catalogueRepo.findByProductNameContainingIgnoreCase(name);
    }
    @Override
    public List<Catalogue> getCatalogueByPriceRange(int minPrice, int maxPrice) {
        return catalogueRepo.findByPriceBetween(minPrice, maxPrice);
    }
    
}
