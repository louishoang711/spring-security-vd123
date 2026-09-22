package vn.iotstar.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.iotstar.security.AppPrincipal;
import vn.iotstar.service.ManagementService;
@Controller public class HomeController {
    private final ManagementService management;
    private final int example;
    public HomeController(ManagementService management, @Value("${app.example:3}") int example) {
        this.management=management; this.example=example;
    }
    @GetMapping("/") String home(Model model, @AuthenticationPrincipal AppPrincipal principal) {
        model.addAttribute("example",example); model.addAttribute("principal",principal);
        model.addAttribute("userCount",management.userCount()); model.addAttribute("productCount",management.productCount());
        return example == 2 ? "home-vd2" : "home";
    }
}
