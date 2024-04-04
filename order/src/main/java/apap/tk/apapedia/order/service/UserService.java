package apap.tk.apapedia.order.service;

import apap.tk.apapedia.order.dto.response.CommonResponseDTO;
import apap.tk.apapedia.order.dto.rest.GetUserRestDTO;

import java.util.UUID;

public interface UserService {
    CommonResponseDTO<?> updateBalance(int amount, String mutation, UUID userId);
    CommonResponseDTO<GetUserRestDTO> getUser();
}
