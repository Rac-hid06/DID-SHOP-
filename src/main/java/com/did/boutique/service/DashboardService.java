package com.did.boutique.service;

import com.did.boutique.model.Produit;
import com.did.boutique.repository.AchatRepository;
import com.did.boutique.repository.LigneAchatRepository;
import com.did.boutique.repository.LivraisonRepository;
import com.did.boutique.repository.ProduitRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final AchatRepository achatRepo;
    private final LivraisonRepository livraisonRepo;
    private final ProduitRepository produitRepo;
    private final LigneAchatRepository ligneAchatRepo;

    public DashboardService(AchatRepository achatRepo, LivraisonRepository livraisonRepo,
                            ProduitRepository produitRepo, LigneAchatRepository ligneAchatRepo) {
        this.achatRepo = achatRepo;
        this.livraisonRepo = livraisonRepo;
        this.produitRepo = produitRepo;
        this.ligneAchatRepo = ligneAchatRepo;
    }

    private LocalDateTime debutJour() {
        return LocalDate.now().atStartOfDay();
    }

    public BigDecimal ventesDuJour() {
        BigDecimal total = achatRepo.totalEntre(debutJour(), debutJour().plusDays(1));
        return total != null ? total : BigDecimal.ZERO;
    }

    public long nombreAchatsDuJour() {
        return achatRepo.countByDateAchatGreaterThanEqualAndDateAchatLessThan(debutJour(), debutJour().plusDays(1));
    }

    public long nombreLivraisonsDuJour() {
        return livraisonRepo.countByDateLivraisonGreaterThanEqualAndDateLivraisonLessThan(debutJour(), debutJour().plusDays(1));
    }

    public List<Produit> produitsStockFaible() {
        return produitRepo.stockFaible();
    }

    /** Top 5 des produits vendus sur les 30 derniers jours. */
    public List<Object[]> topProduits() {
        return ligneAchatRepo.topProduits(LocalDateTime.now().minusDays(30), PageRequest.of(0, 5));
    }
}
