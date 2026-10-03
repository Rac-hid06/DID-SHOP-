package com.did.boutique.service;

import com.did.boutique.model.Categorie;
import com.did.boutique.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository repo;

    public CategorieService(CategorieRepository repo) {
        this.repo = repo;
    }

    public List<Categorie> lister() {
        return repo.findAllByOrderByNom();
    }

    public boolean existe(String nom) {
        return repo.existsByNomIgnoreCase(nom);
    }

    public Categorie creer(String nom) {
        Categorie c = new Categorie();
        c.setNom(nom.trim());
        return repo.save(c);
    }
}
