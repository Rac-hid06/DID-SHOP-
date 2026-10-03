package com.did.boutique.service;

import com.did.boutique.model.*;
import com.did.boutique.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepo;
    private final ProduitRepository produitRepo;
    private final FournisseurRepository fournisseurRepo;
    private final UtilisateurRepository utilisateurRepo;

    public LivraisonService(LivraisonRepository livraisonRepo, ProduitRepository produitRepo,
                            FournisseurRepository fournisseurRepo, UtilisateurRepository utilisateurRepo) {
        this.livraisonRepo = livraisonRepo;
        this.produitRepo = produitRepo;
        this.fournisseurRepo = fournisseurRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    /**
     * Enregistre la livraison d'un grossiste : cree les lignes (avec le prix d'achat)
     * et AUGMENTE le stock de chaque produit. Tout est annule si une ligne est invalide.
     */
    @Transactional
    public Livraison enregistrer(Integer fournisseurId, List<Integer> produitIds, List<Integer> quantites,
                                 List<BigDecimal> prix, String identifiantUtilisateur) {
        if (fournisseurId == null) {
            throw new OperationInvalideException("Choisissez un fournisseur");
        }
        Fournisseur fournisseur = fournisseurRepo.findById(fournisseurId)
                .orElseThrow(() -> new OperationInvalideException("Fournisseur introuvable"));
        Utilisateur utilisateur = utilisateurRepo.findByIdentifiant(identifiantUtilisateur)
                .orElseThrow(() -> new OperationInvalideException("Utilisateur introuvable"));

        Livraison livraison = new Livraison();
        livraison.setDateLivraison(LocalDateTime.now());
        livraison.setFournisseur(fournisseur);
        livraison.setUtilisateur(utilisateur);

        int n = Math.min(produitIds.size(), Math.min(quantites.size(), prix.size()));
        for (int i = 0; i < n; i++) {
            Integer pid = produitIds.get(i);
            if (pid == null) continue;
            Integer qte = quantites.get(i);
            BigDecimal pu = prix.get(i);
            if (qte == null || qte <= 0) {
                throw new OperationInvalideException("La quantité doit être supérieure à 0");
            }
            if (pu == null || pu.signum() < 0) {
                throw new OperationInvalideException("Le prix d'achat doit être renseigné (0 ou plus)");
            }
            Produit p = produitRepo.findByIdForUpdate(pid)
                    .orElseThrow(() -> new OperationInvalideException("Produit introuvable"));
            p.setStock(p.getStock() + qte);   // reception : le stock augmente

            LigneLivraison ligne = new LigneLivraison();
            ligne.setProduit(p);
            ligne.setQuantite(qte);
            ligne.setPrixUnitaire(pu);
            livraison.addLigne(ligne);
        }
        if (livraison.getLignes().isEmpty()) {
            throw new OperationInvalideException("Ajoutez au moins un produit à la livraison");
        }

        livraison.setTotal(livraison.calculerTotal());
        return livraisonRepo.save(livraison);
    }

    public List<Livraison> historique() {
        return livraisonRepo.findAllByOrderByDateLivraisonDesc();
    }

    public Optional<Livraison> trouverDetails(Integer id) {
        return livraisonRepo.findById(id);
    }
}
