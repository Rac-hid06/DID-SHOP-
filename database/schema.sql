-- ============================================================
-- Gestion de boutique alimentaire - DDL PostgreSQL
-- ============================================================

CREATE TABLE utilisateur (
    id              SERIAL PRIMARY KEY,
    nom             VARCHAR(100) NOT NULL,
    prenom          VARCHAR(100) NOT NULL,
    matricule       VARCHAR(30)  NOT NULL UNIQUE,
    sexe            CHAR(1)      NOT NULL CHECK (sexe IN ('M', 'F')),
    date_naissance  DATE         NOT NULL,
    identifiant     VARCHAR(50)  NOT NULL UNIQUE,
    mot_de_passe    VARCHAR(255) NOT NULL,  -- hash BCrypt
    role            VARCHAR(20)  NOT NULL DEFAULT 'CAISSIER'
                    CHECK (role IN ('GERANT', 'CAISSIER'))
);

CREATE TABLE categorie (
    id   SERIAL PRIMARY KEY,
    nom  VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produit (
    id            SERIAL PRIMARY KEY,
    nom           VARCHAR(150)   NOT NULL,
    prix_vente    NUMERIC(12, 2) NOT NULL CHECK (prix_vente >= 0),
    stock         INTEGER        NOT NULL DEFAULT 0 CHECK (stock >= 0),
    seuil_alerte  INTEGER        NOT NULL DEFAULT 5,
    categorie_id  INTEGER REFERENCES categorie (id) ON DELETE SET NULL
);
CREATE INDEX idx_produit_nom ON produit (LOWER(nom));

CREATE TABLE client (
    id         SERIAL PRIMARY KEY,
    nom        VARCHAR(100) NOT NULL,
    telephone  VARCHAR(30)
);

CREATE TABLE fournisseur (
    id         SERIAL PRIMARY KEY,
    nom        VARCHAR(150) NOT NULL,
    telephone  VARCHAR(30),
    adresse    VARCHAR(255)
);

-- Achats effectues par les clients (vente en caisse)
CREATE TABLE achat (
    id             SERIAL PRIMARY KEY,
    date_achat     TIMESTAMP      NOT NULL DEFAULT NOW(),
    total          NUMERIC(12, 2) NOT NULL DEFAULT 0,
    client_id      INTEGER REFERENCES client (id) ON DELETE SET NULL,
    utilisateur_id INTEGER NOT NULL REFERENCES utilisateur (id)
);

CREATE TABLE ligne_achat (
    id             SERIAL PRIMARY KEY,
    achat_id       INTEGER        NOT NULL REFERENCES achat (id) ON DELETE CASCADE,
    produit_id     INTEGER        NOT NULL REFERENCES produit (id),
    quantite       INTEGER        NOT NULL CHECK (quantite > 0),
    prix_unitaire  NUMERIC(12, 2) NOT NULL CHECK (prix_unitaire >= 0)
);

-- Livraisons des grossistes a la boutique
CREATE TABLE livraison (
    id               SERIAL PRIMARY KEY,
    date_livraison   TIMESTAMP      NOT NULL DEFAULT NOW(),
    total            NUMERIC(12, 2) NOT NULL DEFAULT 0,
    fournisseur_id   INTEGER        NOT NULL REFERENCES fournisseur (id),
    utilisateur_id   INTEGER        NOT NULL REFERENCES utilisateur (id)
);

CREATE TABLE ligne_livraison (
    id             SERIAL PRIMARY KEY,
    livraison_id   INTEGER        NOT NULL REFERENCES livraison (id) ON DELETE CASCADE,
    produit_id     INTEGER        NOT NULL REFERENCES produit (id),
    quantite       INTEGER        NOT NULL CHECK (quantite > 0),
    prix_unitaire  NUMERIC(12, 2) NOT NULL CHECK (prix_unitaire >= 0)
);
