package vn.iotstar.controller;
import java.io.IOException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.ProductForm;
import vn.iotstar.security.AppPrincipal;
import vn.iotstar.service.ManagementService;

@Controller @RequestMapping("/products") public class ProductController {
    private final ManagementService management; private final int example;
    public ProductController(ManagementService management,@Value("${app.example:3}") int example) { this.management=management; this.example=example; }
    private void requireExample3() { if (example!=3) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND); }
    @GetMapping String list(@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="0") int page,Model model) {
        requireExample3(); model.addAttribute("products",management.products(keyword,page)); model.addAttribute("keyword",keyword); return "products/list";
    }
    @GetMapping("/new") String create(Model model) { requireExample3(); model.addAttribute("form",new ProductForm()); return "products/form"; }
    @GetMapping("/{id}/edit") String edit(@PathVariable Long id,Model model,@AuthenticationPrincipal AppPrincipal principal) { requireExample3(); model.addAttribute("form",management.editableProductForm(id,principal)); model.addAttribute("id",id); return "products/form"; }
    @PostMapping("/save") String create(@Valid @ModelAttribute("form") ProductForm form,BindingResult errors,
            @RequestParam(required=false) MultipartFile image,@AuthenticationPrincipal AppPrincipal principal) throws IOException {
        requireExample3(); if (errors.hasErrors()) return "products/form";
        management.saveProduct(null,form,image,principal); return "redirect:/products";
    }
    @PostMapping("/{id}/save") String edit(@PathVariable Long id,@Valid @ModelAttribute("form") ProductForm form,BindingResult errors,
            @RequestParam(required=false) MultipartFile image,@AuthenticationPrincipal AppPrincipal principal,Model model) throws IOException {
        requireExample3(); model.addAttribute("id",id); if (errors.hasErrors()) return "products/form";
        management.saveProduct(id,form,image,principal); return "redirect:/products";
    }
    @PostMapping("/{id}/delete") String delete(@PathVariable Long id,@AuthenticationPrincipal AppPrincipal principal) throws IOException {
        requireExample3(); management.deleteProduct(id,principal); return "redirect:/products";
    }
}
