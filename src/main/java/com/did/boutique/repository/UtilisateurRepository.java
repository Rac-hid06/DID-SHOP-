package com.did.boutique.repository;

import com.did.boutique.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByIdentifiant(String identifiant);
    boolean existsByIdentifiant(String identifiant);
    boolean existsByMatricule(String matricule);
}
