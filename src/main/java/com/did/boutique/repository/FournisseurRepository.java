package com.did.boutique.repository;

import com.did.boutique.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    List<Fournisseur> findAllByOrderByNom();
}
