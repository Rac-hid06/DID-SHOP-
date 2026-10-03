# Gestion de boutique alimentaire

Application web Java 25 / Spring Boot (MVC) / PostgreSQL.

## Contenu

- `src/` : code source (model, repository, service, controller, config, templates)
- `database/schema.sql` : script DDL des tables PostgreSQL
- `docs/Rapport_Gestion_Boutique.docx` : rapport
- `pom.xml` : construction Maven

## Installation

1. Installer Java 25, Maven et PostgreSQL. Les commandes ci-dessous supposent PostgreSQL 17 installé dans `C:\Program Files\PostgreSQL\17` et configuré sur le port `5434`.

2. Démarrer PostgreSQL depuis PowerShell ouvert en administrateur :

   ```powershell
   Start-Service postgresql-x64-17
   ```

3. Ouvrir PowerShell dans le dossier `boutique`, puis créer la base et importer son schéma :

   ```powershell
   & "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h 127.0.0.1 -p 5434 -U postgres -d postgres -W -c "CREATE DATABASE boutique;"
   & "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h 127.0.0.1 -p 5434 -U postgres -d boutique -W -f ".\database\schema.sql"
   ```

   La création de la base ne se fait qu'une fois. Si elle existe déjà, ne relance pas la première commande.

4. Dans `src/main/resources/application.properties`, configure l'URL PostgreSQL et le nom du rôle PostgreSQL. Le mot de passe est fourni à l'application par la variable d'environnement `DB_PASSWORD` et ne doit pas être enregistré dans le code.

5. Toujours depuis le dossier `boutique`, sélectionner Java 25 et construire le JAR :

   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot"
   $env:Path = "$env:JAVA_HOME\bin;$env:Path"
   mvn clean package
   ```

   Attendre le message `BUILD SUCCESS`. Reconstruire après toute modification de configuration ou du code.

6. Lancer l'application :

   ```powershell
   $securePassword = Read-Host "Mot de passe PostgreSQL" -AsSecureString
   $env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
   Remove-Variable securePassword
   & "$env:JAVA_HOME\bin\java.exe" -jar .\target\boutique-1.0.0.jar
   ```

7. Ouvrir [http://localhost:8080](http://localhost:8080). Garder la fenêtre PowerShell ouverte pendant l'utilisation.

8. Arrêter l'application en appuyant sur `Ctrl+C` dans la fenêtre où elle tourne. Puis effacer le mot de passe de la session PowerShell avec `Remove-Item Env:DB_PASSWORD`. Si le port `8080` est déjà utilisé, l'application peut être déjà lancée : ne démarre pas une deuxième instance.

## Compte par défaut (créé au premier lancement)

- Identifiant : `admin`
- Mot de passe : `admin123` (à changer ensuite)

## Rôles

- GERANT : tout (utilisateurs, catégories, produits, fournisseurs, livraisons, achats, tableau de bord)
- CAISSIER : caisse (achats + ticket), historique des achats, liste/recherche des produits

## Ordre de test conseillé

Catégories -> Produits -> Fournisseur -> Livraison (le stock monte) -> Achat (le stock baisse, ticket)
-> Historiques -> Tableau de bord -> Utilisateurs (créer un caissier et tester ses droits).
-> Historiques -> Tableau de bord -> Utilisateurs (créer un caissier et tester ses droits).
