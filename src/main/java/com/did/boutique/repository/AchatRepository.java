package com.did.boutique.repository;

import com.did.boutique.model.Achat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AchatRepository extends JpaRepository<Achat, Integer> {

    @EntityGraph(attributePaths = {"client", "utilisateur"})
    List<Achat> findAllByOrderByDateAchatDesc();

    // Detail complet pour le ticket de caisse
    @EntityGraph(attributePaths = {"client", "utilisateur", "lignes", "lignes.produit"})
    @Override
    Optional<Achat> findById(Integer id);

    // Statistiques du tableau de bord
    @Query("select sum(a.total) from Achat a where a.dateAchat >= :debut and a.dateAchat < :fin")
    BigDecimal totalEntre(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    long countByDateAchatGreaterThanEqualAndDateAchatLessThan(LocalDateTime debut, LocalDateTime fin);
}
