package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.dto.request.RegistrationRequestDTO;
import apap.tk.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetUserRestDTO;
import apap.tk.apapedia.frontend.dto.rest.SSORestDTO;
import org.springframework.security.core.context.SecurityContext;

public interface UserService {
    void register(RegistrationRequestDTO dto);
    CommonResponseDTO<?> login(String username);
    CommonResponseDTO<?> withdraw(long amount);
    SSORestDTO validateTicket(String ticket);
    SecurityContext getSecurityContext(String token);
    GetUserRestDTO getUserDetail();
    CommonResponseDTO<?> updateUser(UpdateUserRequestDTO user);
    CommonResponseDTO<?> deleteUser();
}
