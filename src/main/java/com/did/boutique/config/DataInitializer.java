package com.did.boutique.config;

import com.did.boutique.model.Role;
import com.did.boutique.model.Utilisateur;
import com.did.boutique.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/** Cree un compte gerant par defaut si la base ne contient aucun utilisateur. */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner creerGerantParDefaut(UtilisateurRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                Utilisateur u = new Utilisateur();
                u.setNom("Admin");
                u.setPrenom("Gérant");
                u.setMatricule("ADM001");
                u.setSexe('M');
                u.setDateNaissance(LocalDate.of(1990, 1, 1));
                u.setIdentifiant("admin");
                u.setMotDePasse(encoder.encode("admin123"));
                u.setRole(Role.GERANT);
                repo.save(u);
                System.out.println(">>> Compte par defaut cree : admin / admin123 (a changer)");
            }
        };
    }
}
