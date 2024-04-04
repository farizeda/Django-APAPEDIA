package apap.tk.apapedia.user.controller;

import apap.tk.apapedia.user.dto.UserMapper;
import apap.tk.apapedia.user.dto.request.UpdateBalanceRequestDTO;
import apap.tk.apapedia.user.dto.request.UpdateUserRequestDTO;
import apap.tk.apapedia.user.dto.response.CommonResponseDTO;
import apap.tk.apapedia.user.dto.response.GetUserResponseDTO;
import apap.tk.apapedia.user.model.User;
import apap.tk.apapedia.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserMapper userMapper;

    private final UserService userService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @DeleteMapping("")
    public ResponseEntity<CommonResponseDTO<Null>> deleteUser() {
        var user = userService.getCurrentUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User didn't exist!", null));
        }

        userService.deleteUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "User deleted successfully!", null));
    }

    @PutMapping("")
    public ResponseEntity<CommonResponseDTO<Null>> updateUser(@Valid @RequestBody UpdateUserRequestDTO body) {
        var existingUser = userService.getCurrentUser();

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User didn't exist!", null));
        }

        User user;
        if (existingUser.getType().equalsIgnoreCase("Seller")) {
            user = userMapper.updateUserToSeller(body);
        } else {
            user = userMapper.updateUserToCustomer(body);
        }

        String newPassword = body.getNewPassword();
        String oldPassword = body.getOldPassword();

        if (newPassword != null && oldPassword != null) {
            if (newPassword.equals(oldPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDTO<>(false, "Password must be different!", null));
            }

            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponseDTO<>(false, "Wrong password!", null));
            }

            String password = passwordEncoder.encode(newPassword);

            existingUser.setPassword(password);
        }

        userService.updateUser(existingUser, user);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "User updated successfully!", null));
    }

    @GetMapping("")
    public ResponseEntity<CommonResponseDTO<GetUserResponseDTO>> getUser() {

        var user = userService.getCurrentUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User didn't exists!", null));
        }

        var response = userMapper.userToGetUserResponseDTO(user);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "User successfully retrieved!", response));
    }

    @PutMapping("/{userId}/balance")
    public ResponseEntity<CommonResponseDTO<Null>> updateBalance(@PathVariable("userId") UUID userId,  @Valid @RequestBody UpdateBalanceRequestDTO body) {
        Optional<User> user = userService.findUserById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User didn't exist!", null));
        }

        userService.updateBalance(user.get(), body);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Balance updated successfully!", null));
    }
}
