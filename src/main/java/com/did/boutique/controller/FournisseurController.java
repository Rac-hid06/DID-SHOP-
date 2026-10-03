package com.did.boutique.controller;

import com.did.boutique.service.FournisseurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fournisseurs")
public class FournisseurController {

    private final FournisseurService service;

    public FournisseurController(FournisseurService service) {
        this.service = service;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("fournisseurs", service.lister());
        return "fournisseurs/liste";
    }

    @PostMapping
    public String ajouter(@RequestParam String nom,
                          @RequestParam(required = false) String telephone,
                          @RequestParam(required = false) String adresse,
                          RedirectAttributes ra) {
        if (nom == null || nom.isBlank()) {
            ra.addFlashAttribute("erreur", "Le nom du fournisseur est obligatoire");
        } else {
            service.creer(nom, telephone, adresse);
            ra.addFlashAttribute("succes", "Fournisseur ajouté");
        }
        return "redirect:/fournisseurs";
    }
}
