package vn.iotstar.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.RegisterForm;
import vn.iotstar.service.AuthService;

@Controller public class AuthController {
    private final AuthService auth;
    private final int example;
    public AuthController(AuthService auth, @Value("${app.example:3}") int example) { this.auth=auth; this.example=example; }
    @GetMapping("/login") String login(Model model) { model.addAttribute("example",example); return "auth/login"; }
    @GetMapping("/register") String register(Model model) { model.addAttribute("form",new RegisterForm()); return "auth/register"; }
    @PostMapping("/register") String register(@Valid @ModelAttribute("form") RegisterForm form, BindingResult errors,
                                               RedirectAttributes redirect) {
        if (errors.hasErrors()) return "auth/register";
        try { auth.register(form); }
        catch (IllegalArgumentException ex) { errors.reject("register",ex.getMessage()); return "auth/register"; }
        redirect.addAttribute("email",form.getEmail()); return "redirect:/verify-otp";
    }
    @GetMapping("/verify-otp") String verifyPage() { return "auth/verify"; }
    @PostMapping("/verify-otp") String verify(@RequestParam String email, @RequestParam String code, Model model) {
        if (auth.verify(email,code)) return "redirect:/login?verified";
        model.addAttribute("error","Invalid or expired code"); return "auth/verify";
    }
    @GetMapping("/forgot-password") String forgotPage() { return "auth/forgot"; }
    @PostMapping("/forgot-password") String forgot(@RequestParam String email, RedirectAttributes redirect) {
        auth.requestReset(email); redirect.addAttribute("email",email); return "redirect:/reset-password";
    }
    @GetMapping("/reset-password") String resetPage() { return "auth/reset"; }
    @PostMapping("/reset-password") String reset(@RequestParam String email, @RequestParam String code,
                                                  @RequestParam String password, Model model) {
        if (auth.reset(email,code,password)) return "redirect:/login?reset";
        model.addAttribute("error","Invalid or expired code, or password too short"); return "auth/reset";
    }
    @GetMapping("/access-denied") String denied() { return "access-denied"; }
}
