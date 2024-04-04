package apap.tk.apapedia.catalogue.repository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apap.tk.apapedia.catalogue.model.Catalogue;

@Repository
@Transactional
public interface CatalogueRepo extends JpaRepository<Catalogue, UUID>{
    Optional<Catalogue> findCatalogByIdAndSellerId(UUID id, UUID seller);
    List<Catalogue> findBySellerId(UUID sellerId);

    @Query(value = "SELECT c FROM Catalogue c WHERE c.deleted = false")
    List<Catalogue> findAllByOrderByIgnoreCase(Sort sort);
    List<Catalogue> findByProductNameContainingIgnoreCase(String name);
    List<Catalogue> findByPriceBetween(Integer minPrice, Integer maxPrice);

}
