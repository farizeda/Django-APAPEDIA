package apap.tk.apapedia.frontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO <T> {
    boolean isSuccess = true;
    String message;
    T content;
}
