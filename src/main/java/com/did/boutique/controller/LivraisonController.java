package com.did.boutique.controller;

import com.did.boutique.model.Livraison;
import com.did.boutique.service.FournisseurService;
import com.did.boutique.service.LivraisonService;
import com.did.boutique.service.OperationInvalideException;
import com.did.boutique.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/livraisons")
public class LivraisonController {

    private final LivraisonService livraisonService;
    private final FournisseurService fournisseurService;
    private final ProduitService produitService;

    public LivraisonController(LivraisonService livraisonService, FournisseurService fournisseurService,
                               ProduitService produitService) {
        this.livraisonService = livraisonService;
        this.fournisseurService = fournisseurService;
        this.produitService = produitService;
    }

    @GetMapping
    public String historique(Model model) {
        model.addAttribute("livraisons", livraisonService.historique());
        return "livraisons/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        remplirFormulaire(model, List.of(new LigneForm()), null);
        return "livraisons/form";
    }

    @PostMapping
    public String enregistrer(@RequestParam(required = false) Integer fournisseurId,
                              @RequestParam(name = "produitId", required = false) List<Integer> produitIds,
                              @RequestParam(name = "quantite", required = false) List<Integer> quantites,
                              @RequestParam(name = "prixUnitaire", required = false) List<BigDecimal> prix,
                              Principal principal, Model model) {
        List<Integer> pids = produitIds != null ? produitIds : List.of();
        List<Integer> qtes = quantites != null ? quantites : List.of();
        List<BigDecimal> prixList = prix != null ? prix : List.of();
        try {
            Livraison l = livraisonService.enregistrer(fournisseurId, pids, qtes, prixList, principal.getName());
            return "redirect:/livraisons/" + l.getId();
        } catch (OperationInvalideException e) {
            List<LigneForm> lignes = new ArrayList<>();
            for (int i = 0; i < pids.size(); i++) {
                lignes.add(new LigneForm(pids.get(i),
                        i < qtes.size() ? qtes.get(i) : 1,
                        i < prixList.size() ? prixList.get(i) : null));
            }
            if (lignes.isEmpty()) lignes.add(new LigneForm());
            remplirFormulaire(model, lignes, fournisseurId);
            model.addAttribute("erreur", e.getMessage());
            return "livraisons/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Livraison l = livraisonService.trouverDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("livraison", l);
        return "livraisons/detail";
    }

    private void remplirFormulaire(Model model, List<LigneForm> lignes, Integer fournisseurId) {
        model.addAttribute("produits", produitService.rechercher(null));
        model.addAttribute("fournisseurs", fournisseurService.lister());
        model.addAttribute("lignes", lignes);
        model.addAttribute("fournisseurId", fournisseurId);
    }
}
