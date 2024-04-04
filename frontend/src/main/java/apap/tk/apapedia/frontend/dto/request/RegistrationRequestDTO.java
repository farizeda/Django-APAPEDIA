package apap.tk.apapedia.frontend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {
    @NotNull
    String username;

    @NotNull
    @Email
    String email;

    @NotNull
    @Size(min = 8)
    String password;

    @NotNull
    @Size(min = 8)
    String passwordConfirmation;

    String category;
}
