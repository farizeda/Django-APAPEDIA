package apap.tk.apapedia.user.service;

import apap.tk.apapedia.user.dto.response.CommonResponseDTO;
import apap.tk.apapedia.user.dto.rest.CreateCartRestDTO;

import java.util.UUID;

public interface OrderService {
    CommonResponseDTO<CreateCartRestDTO> populateCartId(UUID userId);
}
