package apap.tk.apapedia.catalogue.dto.response;

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

    @Override
    public String toString() {
        return String.format("{\"message\":\"%s\",\"content\":null,\"success\":%s}", this.message, this.isSuccess);
    }
}
