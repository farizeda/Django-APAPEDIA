package apap.tk.apapedia.user.controller;

import apap.tk.apapedia.user.dto.UserMapper;
import apap.tk.apapedia.user.dto.request.LoginRequestDTO;
import apap.tk.apapedia.user.dto.request.LoginSSORequestDTO;
import apap.tk.apapedia.user.dto.request.RegistrationRequestDTO;
import apap.tk.apapedia.user.dto.response.CommonResponseDTO;
import apap.tk.apapedia.user.dto.response.LoginResponseDTO;
import apap.tk.apapedia.user.dto.rest.CreateCartRestDTO;
import apap.tk.apapedia.user.model.Customer;
import apap.tk.apapedia.user.model.User;
import apap.tk.apapedia.user.service.OrderService;
import apap.tk.apapedia.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserMapper userMapper;

    private final UserService userService;

    private final OrderService orderService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AuthController(UserMapper userMapper, UserService userService, OrderService orderService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping("/registration")
    public ResponseEntity<CommonResponseDTO<Null>> register(@Valid @RequestBody RegistrationRequestDTO body, BindingResult bindingResult) {
        try {
            if (bindingResult.hasFieldErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Request body has invalid type or missing fields");
            }

            String category = body.getCategory();

            User user;

            if (category != null) {
                user = userMapper.registerToSeller(body);
            } else {
                user = userMapper.registerToCustomer(body);
                CommonResponseDTO<CreateCartRestDTO> response = orderService.populateCartId(user.getId());

                if (!response.isSuccess()) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Internal server error occurred!");
                }

                ((Customer) user).setCartId(response.getContent().getCartId());
            }

            userService.register(user);

            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "User successfully registered", null));
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains("unique constraint")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CommonResponseDTO<>(false, "User has already registered", null));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDTO<>(false, "Internal server error occurred!", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO body) {
        Optional<User> isUserExists = userService.getUser(body.getIdentity());
        if (isUserExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User with the provided email or username didn't exists!", null));
        }

        var user = isUserExists.get();

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponseDTO<>(false, "Invalid Credentials!", null));
        }

        String token = userService.generateToken(user);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "User successfully authenticated!", new LoginResponseDTO(token)));
    }

    @PostMapping("/login/sso")
    public ResponseEntity<CommonResponseDTO<LoginResponseDTO>> loginSSO(@Valid @RequestBody LoginSSORequestDTO body) {
        Optional<User> isUserExists = userService.getUser(body.getUsername());
        if (isUserExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDTO<>(false, "User with the provided email or username didn't exists!", null));
        }

        String token = userService.generateToken( isUserExists.get());

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "User successfully authenticated!", new LoginResponseDTO(token)));
    }
}
