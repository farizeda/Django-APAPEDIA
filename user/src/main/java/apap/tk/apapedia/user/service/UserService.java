package apap.tk.apapedia.user.service;

import apap.tk.apapedia.user.dto.request.UpdateBalanceRequestDTO;
import apap.tk.apapedia.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User getCurrentUser();
    void register(User user);
    Optional<User> getUser(String identity);
    String generateToken(User user);
    Optional<User> findUserById(UUID id);
    void updateUser(User existingUser, User user);
    void deleteUser(User user);
    void updateBalance(User user, UpdateBalanceRequestDTO body);
}
