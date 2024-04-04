package apap.tk.apapedia.order.controller;

import apap.tk.apapedia.order.dto.response.CommonResponseDTO;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<CommonResponseDTO<Null>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDTO<>(true, "Hello from Order", null));
    }
}
