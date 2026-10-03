package com.did.boutique.repository;

import com.did.boutique.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    boolean existsByNomIgnoreCase(String nom);
    List<Categorie> findAllByOrderByNom();
}
