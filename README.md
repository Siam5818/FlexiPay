FlexiPay - Gestion des Cartes Bancaires et Paiements

Description: 
FlexiPay est un système de gestion des cartes bancaires permettant aux clients de demander et d'utiliser des cartes de paiement pour effectuer des achats, des retraits, et de gérer leurs transactions en toute sécurité. 
Ce projet inclut des fonctionnalités avancées, telles que la double authentification, les systèmes de cashback et de fidélité, ainsi que la gestion des abonnements.

Fonctionnalités principales

Pour les clients :

Demande, activation et suspension des cartes bancaires.

Paiements en ligne et retraits sécurisés grâce à un code PIN et un système OTP (double authentification).

Historique détaillé des transactions avec statuts de validation ou de rejet.

Gestion des abonnements (Netflix, Spotify, etc.), avec possibilité de visualiser et annuler les abonnements.

Plafonds de dépenses pour contrôler l'utilisation de la carte.

Système de cashback et fidélité : cumulez des points pour chaque transaction et échangez-les contre des récompenses.

Signalement des litiges ou paiements frauduleux.

Pour les administrateurs :

Suivi et analyse des transactions suspectes via un tableau de bord.

Gestion des litiges : validation ou rejet des réclamations.

Visualisation des abonnements et des paiements récurrents.

Gestion sécurisée des données des clients et des transactions.

Architecture et technologies

src/
└── main/
├── java/
│   └── sn/groupeisi/flexipay/
│       ├── controllers/    # Contrôleurs pour gérer les interactions utilisateur
│       ├── entities/       # Modèles de données mappés aux tables de la base
│       ├── enums/          # Enums pour les constantes spécifiques
│       ├── interfaces/     # Interfaces DAO pour les opérations CRUD
│       ├── services/       # Logique métier
│       ├── utils/          # Classes utilitaires (JPA, validation, etc.)
│       └── App.java        # Point d'entrée principal
└── resources/
├── META-INF/persistence.xml  # Configuration JPA
└── fxml/                     # Fichiers d'interface utilisateur JavaFX

Technologies utilisées

Java SE 17+

Jakarta Persistence API (JPA) pour la gestion des bases de données.

Hibernate en tant que ORM.

PostgreSQL comme système de gestion de base de données.

JavaFX pour l'interface utilisateur.

Lombok pour réduire le code répétitif (constructeurs, getters, setters).

HikariCP (optionnel) pour le pool de connexions en production.

Base de données

Modèle relationnel

Client : Stocke les informations des clients.

CarteBancaire : Cartes associées aux clients.

Transaction : Paiements et retraits avec suivi du statut.

Abonnement : Paiements récurrents liés aux cartes.

AuthentificationTransaction : Gestion des OTP pour la double authentification.

Litige : Suivi des réclamations ou fraudes signalées.

Fidélité : Points accumulés pour chaque paiement.

Admin : Gestion des administrateurs.

Installation et exécution

Prérequis :

JDK 17+

PostgreSQL installé et configuré

Maven pour gérer les dépendances

Clonez le projet : https://github.com/Siam5818/FlexiPay.git

Configurez la base de données PostgreSQL et mettez à jour le fichier persistence.xml.

Compilez et exécutez l'application :

mvn clean install

java -jar target/FlexiPay.jar

Auteur

Mohamed Anzize SIHAMOUDINE

Licence

Ce projet est sous licence MIT. Consultez le fichier LICENSE pour plus d'informations.