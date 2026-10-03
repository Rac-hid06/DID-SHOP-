package com.did.boutique.repository;

import com.did.boutique.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findFirstByNomIgnoreCase(String nom);
}
