package com.did.boutique.controller;

import com.did.boutique.model.Achat;
import com.did.boutique.service.AchatService;
import com.did.boutique.service.OperationInvalideException;
import com.did.boutique.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/achats")
public class AchatController {

    private final AchatService achatService;
    private final ProduitService produitService;

    public AchatController(AchatService achatService, ProduitService produitService) {
        this.achatService = achatService;
        this.produitService = produitService;
    }

    @GetMapping
    public String historique(Model model) {
        model.addAttribute("achats", achatService.historique());
        return "achats/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        remplirFormulaire(model, List.of(new LigneForm()), null);
        return "achats/form";
    }

    @PostMapping
    public String enregistrer(@RequestParam(name = "produitId", required = false) List<Integer> produitIds,
                              @RequestParam(name = "quantite", required = false) List<Integer> quantites,
                              @RequestParam(required = false) String clientNom,
                              Principal principal, Model model) {
        List<Integer> pids = produitIds != null ? produitIds : List.of();
        List<Integer> qtes = quantites != null ? quantites : List.of();
        try {
            Achat achat = achatService.enregistrer(pids, qtes, clientNom, principal.getName());
            return "redirect:/achats/" + achat.getId() + "/ticket";
        } catch (OperationInvalideException e) {
            // On reaffiche le formulaire avec les lignes saisies
            List<LigneForm> lignes = new ArrayList<>();
            for (int i = 0; i < pids.size(); i++) {
                lignes.add(new LigneForm(pids.get(i), i < qtes.size() ? qtes.get(i) : 1));
            }
            if (lignes.isEmpty()) lignes.add(new LigneForm());
            remplirFormulaire(model, lignes, clientNom);
            model.addAttribute("erreur", e.getMessage());
            return "achats/form";
        }
    }

    @GetMapping("/{id}/ticket")
    public String ticket(@PathVariable Integer id, Model model) {
        Achat achat = achatService.trouverDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("achat", achat);
        return "achats/ticket";
    }

    private void remplirFormulaire(Model model, List<LigneForm> lignes, String clientNom) {
        model.addAttribute("produits", produitService.rechercher(null));
        model.addAttribute("lignes", lignes);
        model.addAttribute("clientNom", clientNom);
    }
}
