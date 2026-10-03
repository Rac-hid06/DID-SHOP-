package com.did.boutique.controller;

import java.math.BigDecimal;

/** Une ligne de formulaire (achat ou livraison) : produit, quantite et, pour les livraisons, prix d'achat. */
public class LigneForm {
    private Integer produitId;
    private Integer quantite = 1;
    private BigDecimal prixUnitaire;

    public LigneForm() {}

    public LigneForm(Integer produitId, Integer quantite) {
        this.produitId = produitId;
        this.quantite = quantite;
    }

    public LigneForm(Integer produitId, Integer quantite, BigDecimal prixUnitaire) {
        this(produitId, quantite);
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getProduitId() { return produitId; }
    public void setProduitId(Integer produitId) { this.produitId = produitId; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}
