package com.did.boutique.controller;

import com.did.boutique.service.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("categories", service.lister());
        return "categories/liste";
    }

    @PostMapping
    public String ajouter(@RequestParam String nom, RedirectAttributes ra) {
        if (nom == null || nom.isBlank()) {
            ra.addFlashAttribute("erreur", "Le nom de la catégorie est obligatoire");
        } else if (service.existe(nom.trim())) {
            ra.addFlashAttribute("erreur", "Cette catégorie existe déjà");
        } else {
            service.creer(nom);
            ra.addFlashAttribute("succes", "Catégorie ajoutée");
        }
        return "redirect:/categories";
    }
}
