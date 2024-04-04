package apap.tk.apapedia.user.service;


import apap.tk.apapedia.user.dto.request.UpdateBalanceRequestDTO;
import apap.tk.apapedia.user.model.User;
import apap.tk.apapedia.user.repository.UserRepo;
import apap.tk.apapedia.user.security.SecurityUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final UserRepo userRepo;
    private final JWTService jwtService;

    public UserServiceImpl(UserRepo userRepo, JWTService jwtService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @Override
    public User getCurrentUser() {
        SecurityUserDetails authentication = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = findUserById(UUID.fromString(authentication.getJwtClaims().getSubject()));

        return user.orElse(null);

    }

    @Override
    public void register(User user) {
        userRepo.save(user);
    }

    @Override
    public Optional<User> getUser(String identity) {
        if (isValidEmail(identity)) {
            return userRepo.getByEmailIgnoreCase(identity);
        } else {
            return userRepo.getByUsername(identity);
        }
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("userName", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("address", user.getAddress());
        claims.put("balance", user.getBalance());
        claims.put("role", user.getType());

        return Jwts.builder().setClaims(claims).setSubject(user.getId().toString()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + jwtService.getJwtExpiration())).signWith(SignatureAlgorithm.HS256, jwtService.getSignature()).compact();
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepo.findById(id);
    }

    @Override
    public void updateUser(User existingUser, User user) {
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setAddress(user.getAddress());
        existingUser.setEmail(user.getEmail());

        register(existingUser);
    }

    @Override
    public void deleteUser(User user) {
        user.setDeleted(Boolean.TRUE);
        register(user);
    }

    @Override
    public void updateBalance(User user, UpdateBalanceRequestDTO body) {
        long balance;
        if (body.getMutation().equalsIgnoreCase("DEBIT")) {
            balance = user.getBalance() + body.getAmount();
        } else {
            balance = user.getBalance() - body.getAmount();
        }

        user.setBalance(balance);
        register(user);
    }

    private static boolean isValidEmail(String email) {
        var pattern = Pattern.compile(EMAIL_REGEX);
        var matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
