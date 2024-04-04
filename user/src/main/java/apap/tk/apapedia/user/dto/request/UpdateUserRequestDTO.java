package apap.tk.apapedia.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {
    private String username;
    private String name;
    private String email;
    private String address;
    private String newPassword;
    private String oldPassword;
}
