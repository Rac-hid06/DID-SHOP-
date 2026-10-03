package com.did.boutique.controller;

import com.did.boutique.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("ventesJour", service.ventesDuJour());
        model.addAttribute("achatsJour", service.nombreAchatsDuJour());
        model.addAttribute("livraisonsJour", service.nombreLivraisonsDuJour());
        model.addAttribute("stockFaible", service.produitsStockFaible());
        model.addAttribute("topProduits", service.topProduits());
        return "index";
    }
}
