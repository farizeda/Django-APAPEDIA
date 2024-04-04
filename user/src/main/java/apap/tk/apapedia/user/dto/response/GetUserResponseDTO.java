package apap.tk.apapedia.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponseDTO {
    private String id;
    private String name;
    private String username;
    private String email;
    private int balance;
    private String address;
}
