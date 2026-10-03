package com.did.boutique.service;

/** Erreur metier (stock insuffisant, quantite invalide...). Provoque l'annulation de la transaction. */
public class OperationInvalideException extends RuntimeException {
    public OperationInvalideException(String message) {
        super(message);
    }
}
