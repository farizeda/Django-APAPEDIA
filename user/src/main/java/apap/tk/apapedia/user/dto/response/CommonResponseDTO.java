package apap.tk.apapedia.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO <T> {
    @JsonProperty("success")
    boolean isSuccess = true;
    String message;
    T content;

    @Override
    public String toString() {
        return String.format("{\"message\":\"%s\",\"content\":null,\"success\":%s}", this.message, this.isSuccess);
    }
}
