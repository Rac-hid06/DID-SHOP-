package com.did.boutique.service;

import com.did.boutique.model.Utilisateur;
import com.did.boutique.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurDetailsService implements UserDetailsService {

    private final UtilisateurRepository repo;

    public UtilisateurDetailsService(UtilisateurRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String identifiant) throws UsernameNotFoundException {
        Utilisateur u = repo.findByIdentifiant(identifiant)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + identifiant));
        return User.withUsername(u.getIdentifiant())
                .password(u.getMotDePasse())
                .roles(u.getRole().name())
                .build();
    }
}
