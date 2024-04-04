package apap.tk.apapedia.frontend.controller;

import apap.tk.apapedia.frontend.dto.request.RegistrationRequestDTO;
import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.LoginRestDTO;
import apap.tk.apapedia.frontend.dto.rest.SSORestDTO;
import apap.tk.apapedia.frontend.security.SecurityUserDetails;
import apap.tk.apapedia.frontend.service.CommonService;
import apap.tk.apapedia.frontend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
public class AuthController {
    private final UserService userService;
    private final CommonService commonService;

    public AuthController(UserService userService, CommonService commonService) {
        this.userService = userService;
        this.commonService = commonService;
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        List<Map<String, String>> options = List.of(
                Map.ofEntries(
                        Map.entry("value", "regular"),
                        Map.entry("label", "Regular")
                ),
                Map.ofEntries(
                        Map.entry("value", "official_store"),
                        Map.entry("label", "Official Store")
                )
        );

        model.addAttribute("dto", new RegistrationRequestDTO());
        model.addAttribute("options", options);
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute RegistrationRequestDTO dto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return commonService.validate(bindingResult, redirectAttrs, "redirect:/registration");
        }

        if (!dto.getPassword().equalsIgnoreCase(dto.getPasswordConfirmation()) ) {
            redirectAttrs.addFlashAttribute("error", "Password didn't match!");
            return "redirect:/registration";
        }

        try {
            userService.register(dto);
        }catch (WebClientResponseException e) {
            redirectAttrs.addFlashAttribute("error", Objects.requireNonNull(e.getResponseBodyAs(CommonResponseDTO.class)).getMessage());

            return "redirect:/registration";
        }

        redirectAttrs.addFlashAttribute("success", "Successfully registered!");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "ticket", required = false) String ticket,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs
    ) {
        try {
            SSORestDTO ssoResponse = userService.validateTicket(ticket);
            var payload = ssoResponse.getAuthenticationSuccess();
            if (payload == null) {
                redirectAttrs.addFlashAttribute("message", "Oops! SSO UI gone wrong");
                return "redirect:/";
            }

            CommonResponseDTO<?> response = userService.login(payload.getUser());

            if (!response.isSuccess()) {
                redirectAttrs.addFlashAttribute("error", response.getMessage());
                return "redirect:/login";
            }

            String token = ((LoginRestDTO) response.getContent()).getToken();

            var securityContext = userService.getSecurityContext(token);
            var principals = ((SecurityUserDetails) securityContext.getAuthentication().getPrincipal()).getJwtClaims();

            var httpSession = request.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token);

            redirectAttrs.addFlashAttribute("success", "Welcome! " + principals.get("userName"));
            return "redirect:/";
        }catch (WebClientResponseException e) {
            redirectAttrs.addFlashAttribute("error", Objects.requireNonNull(e.getResponseBodyAs(CommonResponseDTO.class)).getMessage());

            return "redirect:/";
        }

    }
}
