package apap.tk.apapedia.frontend.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserRestDTO {
    private String id;
    private String name;
    private String username;
    private String email;
    private int balance;
    private String address;
}
