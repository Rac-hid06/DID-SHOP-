package com.did.boutique.repository;

import com.did.boutique.model.Produit;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Integer> {

    // EntityGraph : charge la categorie en meme temps (open-in-view est desactive)
    @EntityGraph(attributePaths = "categorie")
    List<Produit> findAllByOrderByNom();

    @EntityGraph(attributePaths = "categorie")
    List<Produit> findByNomContainingIgnoreCaseOrderByNom(String nom);

    @EntityGraph(attributePaths = "categorie")
    @Override
    Optional<Produit> findById(Integer id);

    // Verrou en ecriture : evite deux ventes simultanees sur le meme stock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produit p where p.id = :id")
    Optional<Produit> findByIdForUpdate(@Param("id") Integer id);

    // Produits dont le stock a atteint le seuil d'alerte
    @Query("select p from Produit p where p.stock <= p.seuilAlerte order by p.stock, p.nom")
    List<Produit> stockFaible();
}
