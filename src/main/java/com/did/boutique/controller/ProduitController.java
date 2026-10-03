package com.did.boutique.controller;

import com.did.boutique.model.Produit;
import com.did.boutique.service.CategorieService;
import com.did.boutique.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;
    private final CategorieService categorieService;

    public ProduitController(ProduitService produitService, CategorieService categorieService) {
        this.produitService = produitService;
        this.categorieService = categorieService;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("produits", produitService.rechercher(q));
        model.addAttribute("q", q);
        return "produits/liste";
    }

    @GetMapping("/nouveau")
    @PreAuthorize("hasRole('GERANT')")
    public String nouveau(Model model) {
        Produit p = new Produit();
        p.setStock(0);
        p.setSeuilAlerte(5);
        model.addAttribute("produit", p);
        model.addAttribute("categories", categorieService.lister());
        return "produits/form";
    }

    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasRole('GERANT')")
    public String modifier(@PathVariable Integer id, Model model) {
        Produit p = produitService.trouver(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("produit", p);
        model.addAttribute("categorieId", p.getCategorie() != null ? p.getCategorie().getId() : null);
        model.addAttribute("categories", categorieService.lister());
        return "produits/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('GERANT')")
    public String enregistrer(@Valid @ModelAttribute("produit") Produit produit, BindingResult result,
                              @RequestParam(required = false) Integer categorieId,
                              Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categorieId", categorieId);
            model.addAttribute("categories", categorieService.lister());
            return "produits/form";
        }
        produitService.enregistrer(produit, categorieId);
        ra.addFlashAttribute("succes", "Produit enregistré avec succès");
        return "redirect:/produits";
    }
}
