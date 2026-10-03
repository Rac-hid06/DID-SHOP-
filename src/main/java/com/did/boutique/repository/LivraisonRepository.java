package com.did.boutique.repository;

import com.did.boutique.model.Livraison;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface LivraisonRepository extends JpaRepository<Livraison, Integer> {

    @EntityGraph(attributePaths = {"fournisseur", "utilisateur"})
    List<Livraison> findAllByOrderByDateLivraisonDesc();

    @EntityGraph(attributePaths = {"fournisseur", "utilisateur", "lignes", "lignes.produit"})
    @Override
    Optional<Livraison> findById(Integer id);

    long countByDateLivraisonGreaterThanEqualAndDateLivraisonLessThan(LocalDateTime debut, LocalDateTime fin);
}
