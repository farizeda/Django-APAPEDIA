package apap.tk.apapedia.frontend.controller;

import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCataloguesRestDTO;
import apap.tk.apapedia.frontend.service.CatalogueService;
import apap.tk.apapedia.frontend.service.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final CommonService commonService;
    private final CatalogueService catalogueService;

    public HomeController(
            CommonService commonService,
            CatalogueService catalogueService
    ) {
        this.commonService = commonService;
        this.catalogueService = catalogueService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        var user = commonService.getAuthenticatedUser();

        CommonResponseDTO<List<GetCataloguesRestDTO>> commonResponse = catalogueService.getCatalogues(user);

        List<GetCataloguesRestDTO> catalogues = commonResponse.getContent();

        model.addAttribute("catalogues", catalogues);
        model.addAttribute("user", user);
        return "catalogue/index";
    }
}
