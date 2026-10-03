package com.did.boutique.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dateAchat;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "achat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneAchat> lignes = new ArrayList<>();

    public Achat() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDateTime dateAchat) { this.dateAchat = dateAchat; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public List<LigneAchat> getLignes() { return lignes; }
    public void setLignes(List<LigneAchat> lignes) { this.lignes = lignes; }

    public void addLigne(LigneAchat ligne) {
        ligne.setAchat(this);
        lignes.add(ligne);
    }

    public BigDecimal calculerTotal() {
        return lignes.stream()
                .map(LigneAchat::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
