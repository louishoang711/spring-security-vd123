package vn.iotstar.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.UserForm;
import vn.iotstar.service.ManagementService;

@Controller @RequestMapping("/users") public class UserController {
    private final ManagementService management; private final int example;
    public UserController(ManagementService management,@Value("${app.example:3}") int example) { this.management=management; this.example=example; }
    private void requireExample3() { if (example!=3) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND); }
    @GetMapping String list(@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="0") int page,Model model) {
        requireExample3(); model.addAttribute("users",management.users(keyword,page)); model.addAttribute("keyword",keyword); return "users/list";
    }
    @GetMapping("/new") String create(Model model) { requireExample3(); model.addAttribute("form",new UserForm()); return "users/form"; }
    @GetMapping("/{id}/edit") String edit(@PathVariable Long id,Model model) { requireExample3(); model.addAttribute("form",management.userForm(id)); model.addAttribute("id",id); return "users/form"; }
    @PostMapping("/save") String create(@Valid @ModelAttribute("form") UserForm form,BindingResult errors,Model model) {
        requireExample3(); if (errors.hasErrors()) return "users/form";
        try { management.saveUser(null,form); return "redirect:/users"; }
        catch (IllegalArgumentException ex) { errors.reject("save",ex.getMessage()); return "users/form"; }
    }
    @PostMapping("/{id}/save") String edit(@PathVariable Long id,@Valid @ModelAttribute("form") UserForm form,BindingResult errors,Model model) {
        requireExample3(); model.addAttribute("id",id); if (errors.hasErrors()) return "users/form";
        try { management.saveUser(id,form); return "redirect:/users"; }
        catch (IllegalArgumentException ex) { errors.reject("save",ex.getMessage()); return "users/form"; }
    }
    @PostMapping("/{id}/delete") String delete(@PathVariable Long id,RedirectAttributes redirect) {
        requireExample3(); try { management.deleteUser(id); }
        catch (IllegalArgumentException ex) { redirect.addFlashAttribute("error",ex.getMessage()); }
        return "redirect:/users";
    }
}
