package apap.tk.apapedia.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import apap.tk.apapedia.catalogue.model.Category;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface CategoryRepo extends JpaRepository<Category, UUID> {
}
