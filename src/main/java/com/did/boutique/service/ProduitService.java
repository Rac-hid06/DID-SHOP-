package com.did.boutique.service;

import com.did.boutique.model.Produit;
import com.did.boutique.repository.CategorieRepository;
import com.did.boutique.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository repo;
    private final CategorieRepository categorieRepo;

    public ProduitService(ProduitRepository repo, CategorieRepository categorieRepo) {
        this.repo = repo;
        this.categorieRepo = categorieRepo;
    }

    /** Recherche par nom (insensible a la casse) ; sans critere, retourne tous les produits. */
    public List<Produit> rechercher(String nom) {
        if (nom == null || nom.isBlank()) {
            return repo.findAllByOrderByNom();
        }
        return repo.findByNomContainingIgnoreCaseOrderByNom(nom.trim());
    }

    public Optional<Produit> trouver(Integer id) {
        return repo.findById(id);
    }

    public Produit enregistrer(Produit produit, Integer categorieId) {
        produit.setCategorie(categorieId == null ? null : categorieRepo.getReferenceById(categorieId));
        return repo.save(produit);
    }
}
