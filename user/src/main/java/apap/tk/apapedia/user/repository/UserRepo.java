package apap.tk.apapedia.user.repository;

import apap.tk.apapedia.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> getByEmailIgnoreCase(String email);
    Optional<User> getByUsername(String username);
}
