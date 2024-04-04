package apap.tk.apapedia.user.dto;

import apap.tk.apapedia.user.dto.request.RegistrationRequestDTO;
import apap.tk.apapedia.user.dto.request.UpdateUserRequestDTO;
import apap.tk.apapedia.user.dto.response.GetUserResponseDTO;
import apap.tk.apapedia.user.model.Customer;
import apap.tk.apapedia.user.model.Seller;
import apap.tk.apapedia.user.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mapping(target = "password", ignore = true)
    Customer registerToCustomer(RegistrationRequestDTO dto);

    @AfterMapping
    default void completeCustomerData(RegistrationRequestDTO dto, @MappingTarget Customer user) {
        String password = passwordEncoder.encode(dto.getPassword());
        user.setPassword(password);
        user.setId(UUID.randomUUID());
    }

    @Mapping(target = "password", ignore = true)
    Seller registerToSeller(RegistrationRequestDTO dto);

    @AfterMapping
    default void completeSellerData(RegistrationRequestDTO dto, @MappingTarget Seller user) {
        String password = passwordEncoder.encode(dto.getPassword());
        user.setPassword(password);
    }

    GetUserResponseDTO userToGetUserResponseDTO(User user);

    @Mapping(target = "password", ignore = true)
    Seller updateUserToSeller(UpdateUserRequestDTO dto);

    @Mapping(target = "password", ignore = true)
    Customer updateUserToCustomer(UpdateUserRequestDTO dto);
}
