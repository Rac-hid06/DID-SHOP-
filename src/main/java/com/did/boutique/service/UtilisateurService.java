package com.did.boutique.service;

import com.did.boutique.model.Role;
import com.did.boutique.model.Utilisateur;
import com.did.boutique.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder encoder;

    public UtilisateurService(UtilisateurRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<Utilisateur> lister() {
        return repo.findAll();
    }

    public boolean identifiantExiste(String identifiant) {
        return repo.existsByIdentifiant(identifiant);
    }

    public boolean matriculeExiste(String matricule) {
        return repo.existsByMatricule(matricule);
    }

    /** Hache le mot de passe (BCrypt) puis enregistre l'utilisateur. */
    public Utilisateur creer(Utilisateur u) {
        u.setMotDePasse(encoder.encode(u.getMotDePasse()));
        if (u.getRole() == null) {
            u.setRole(Role.CAISSIER);
        }
        return repo.save(u);
    }
}
