package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.model.User;
import apap.tk.apapedia.frontend.security.SecurityUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Service
public class CommonService {
    public String validate(BindingResult bindingResult, RedirectAttributes redirectAttrs, String target) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        String errorMessage = "";

        for (FieldError error: errors) {
            errorMessage += error.getField() + " " + error.getDefaultMessage() + "\n\n";
        }

        redirectAttrs.addFlashAttribute("error", errorMessage);
        return target;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        Claims details = ((SecurityUserDetails) authentication.getPrincipal()).getJwtClaims();

        return User.builder()
                .id(UUID.fromString(details.getSubject()))
                .name((String) details.get("name"))
                .username((String) details.get("userName"))
                .email((String) details.get("email"))
                .address((String) details.get("address"))
                .balance(Long.valueOf((Integer) details.get("balance")))
                .type((String) details.get("role"))
                .build();
    }
}
