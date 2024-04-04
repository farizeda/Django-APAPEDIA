package apap.tk.apapedia.frontend.dto;

import apap.tk.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetUserRestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UpdateUserRequestDTO userToUpdateUserRequestDTO(GetUserRestDTO user);
}
