package com.did.boutique.service;

import com.did.boutique.model.*;
import com.did.boutique.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class AchatService {

    private final AchatRepository achatRepo;
    private final ProduitRepository produitRepo;
    private final ClientRepository clientRepo;
    private final UtilisateurRepository utilisateurRepo;

    public AchatService(AchatRepository achatRepo, ProduitRepository produitRepo,
                        ClientRepository clientRepo, UtilisateurRepository utilisateurRepo) {
        this.achatRepo = achatRepo;
        this.produitRepo = produitRepo;
        this.clientRepo = clientRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    /**
     * Enregistre l'achat d'un client : cree les lignes, fige les prix, decremente le stock.
     * Tout est annule (rollback) si un seul produit a un stock insuffisant.
     */
    @Transactional
    public Achat enregistrer(List<Integer> produitIds, List<Integer> quantites,
                             String clientNom, String identifiantCaissier) {
        // Regroupe les doublons (meme produit sur 2 lignes) ; TreeMap = ordre stable des verrous
        Map<Integer, Integer> panier = new TreeMap<>();
        int n = Math.min(produitIds.size(), quantites.size());
        for (int i = 0; i < n; i++) {
            Integer pid = produitIds.get(i);
            Integer qte = quantites.get(i);
            if (pid == null) continue;
            if (qte == null || qte <= 0) {
                throw new OperationInvalideException("La quantité doit être supérieure à 0");
            }
            panier.merge(pid, qte, Integer::sum);
        }
        if (panier.isEmpty()) {
            throw new OperationInvalideException("Ajoutez au moins un produit à l'achat");
        }

        Utilisateur caissier = utilisateurRepo.findByIdentifiant(identifiantCaissier)
                .orElseThrow(() -> new OperationInvalideException("Utilisateur introuvable"));

        Achat achat = new Achat();
        achat.setDateAchat(LocalDateTime.now());
        achat.setUtilisateur(caissier);
        if (clientNom != null && !clientNom.isBlank()) {
            String nom = clientNom.trim();
            achat.setClient(clientRepo.findFirstByNomIgnoreCase(nom).orElseGet(() -> {
                Client c = new Client();
                c.setNom(nom);
                return clientRepo.save(c);
            }));
        }

        for (Map.Entry<Integer, Integer> e : panier.entrySet()) {
            Produit p = produitRepo.findByIdForUpdate(e.getKey())
                    .orElseThrow(() -> new OperationInvalideException("Produit introuvable"));
            int qte = e.getValue();
            if (p.getStock() < qte) {
                throw new OperationInvalideException("Stock insuffisant pour « " + p.getNom()
                        + " » (disponible : " + p.getStock() + ", demandé : " + qte + ")");
            }
            p.setStock(p.getStock() - qte);

            LigneAchat ligne = new LigneAchat();
            ligne.setProduit(p);
            ligne.setQuantite(qte);
            ligne.setPrixUnitaire(p.getPrixVente());   // prix fige au moment de l'achat
            achat.addLigne(ligne);
        }

        achat.setTotal(achat.calculerTotal());
        return achatRepo.save(achat);
    }

    public List<Achat> historique() {
        return achatRepo.findAllByOrderByDateAchatDesc();
    }

    public Optional<Achat> trouverDetails(Integer id) {
        return achatRepo.findById(id);
    }
}
