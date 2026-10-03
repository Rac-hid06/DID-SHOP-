package com.did.boutique.service;

import com.did.boutique.model.Fournisseur;
import com.did.boutique.repository.FournisseurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FournisseurService {

    private final FournisseurRepository repo;

    public FournisseurService(FournisseurRepository repo) {
        this.repo = repo;
    }

    public List<Fournisseur> lister() {
        return repo.findAllByOrderByNom();
    }

    public Fournisseur creer(String nom, String telephone, String adresse) {
        Fournisseur f = new Fournisseur();
        f.setNom(nom.trim());
        f.setTelephone(telephone == null || telephone.isBlank() ? null : telephone.trim());
        f.setAdresse(adresse == null || adresse.isBlank() ? null : adresse.trim());
        return repo.save(f);
    }
}
