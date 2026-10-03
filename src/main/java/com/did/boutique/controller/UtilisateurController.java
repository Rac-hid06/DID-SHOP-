package com.did.boutique.controller;

import com.did.boutique.model.Role;
import com.did.boutique.model.Utilisateur;
import com.did.boutique.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("utilisateurs", service.lister());
        return "utilisateurs/liste";
    }

    @GetMapping("/nouveau")
    public String formulaire(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        model.addAttribute("roles", Role.values());
        return "utilisateurs/form";
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
                              BindingResult result, Model model, RedirectAttributes ra) {
        if (service.identifiantExiste(utilisateur.getIdentifiant())) {
            result.rejectValue("identifiant", "doublon", "Cet identifiant est déjà utilisé");
        }
        if (service.matriculeExiste(utilisateur.getMatricule())) {
            result.rejectValue("matricule", "doublon", "Ce numéro matricule existe déjà");
        }
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "utilisateurs/form";
        }
        service.creer(utilisateur);
        ra.addFlashAttribute("succes", "Utilisateur ajouté avec succès");
        return "redirect:/utilisateurs";
    }
}
