package apap.tk.apapedia.frontend.controller;

import apap.tk.apapedia.frontend.dto.UserMapper;
import apap.tk.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import apap.tk.apapedia.frontend.dto.request.WithdrawRequestDTO;
import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetUserRestDTO;
import apap.tk.apapedia.frontend.service.CommonService;
import apap.tk.apapedia.frontend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;
    private final CommonService commonService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper, CommonService commonService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.commonService = commonService;
    }

    @GetMapping("")
    public String getProfile(Model model) {
        GetUserRestDTO userDetail = userService.getUserDetail();
        var withdraw = new WithdrawRequestDTO();

        model.addAttribute("userDetail", userDetail);
        model.addAttribute("withdraw", withdraw);
        model.addAttribute("user", commonService.getAuthenticatedUser());
        return "user/index";
    }

    @PostMapping("withdraw")
    public String withdrawFee(
            @Valid @ModelAttribute WithdrawRequestDTO body,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "Invalid Amount!");
            return "redirect:/profile";
        }

        CommonResponseDTO<?> response = userService.withdraw(body.getAmount());

        if (!response.isSuccess()) {
            redirectAttrs.addFlashAttribute("error", "Withdraw failed!");
            return "redirect:/profile";
        }


        redirectAttrs.addFlashAttribute("success", "Withdraw success!");
        return "redirect:/profile";
    }

    @GetMapping("update")
    public String updateProfileForm(Model model) {
        GetUserRestDTO userDetail = userService.getUserDetail();
        var dto = userMapper.userToUpdateUserRequestDTO(userDetail);

        model.addAttribute("dto", dto);
        model.addAttribute("user", commonService.getAuthenticatedUser());
        return "user/update";
    }

    @PostMapping("update")
    public String updateProfile(
            @Valid @ModelAttribute UpdateUserRequestDTO body,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "Invalid Input!");
            return "redirect:/profile";
        }

        CommonResponseDTO<?> response = userService.updateUser(body);

        if (!response.isSuccess()) {
            redirectAttrs.addFlashAttribute("error", "Update profile failed!");
            return "redirect:/profile";
        }


        redirectAttrs.addFlashAttribute("success", "Update profile success!");
        return "redirect:/profile";
    }

    @GetMapping("delete")
    public String delete(
            RedirectAttributes redirectAttrs
    ) {
        CommonResponseDTO<?> response = userService.deleteUser();

        if (!response.isSuccess()) {
            redirectAttrs.addFlashAttribute("error", "Delete profile failed!");
            return "redirect:/profile";
        }

        return "redirect:/logout";
    }
}
