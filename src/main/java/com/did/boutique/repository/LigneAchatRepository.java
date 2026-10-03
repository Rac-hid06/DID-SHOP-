package com.did.boutique.repository;

import com.did.boutique.model.LigneAchat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LigneAchatRepository extends JpaRepository<LigneAchat, Integer> {

    /** Produits les plus vendus depuis une date : [nom, quantite totale vendue]. */
    @Query("""
           select l.produit.nom, sum(l.quantite)
           from LigneAchat l
           where l.achat.dateAchat >= :depuis
           group by l.produit.id, l.produit.nom
           order by sum(l.quantite) desc
           """)
    List<Object[]> topProduits(@Param("depuis") LocalDateTime depuis, Pageable pageable);
}
