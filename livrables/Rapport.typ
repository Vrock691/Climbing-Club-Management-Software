#set document(
  title: "Rapport",
  author: "Maxime Berger - Valentin Mary",
)

#set page(
  paper: "a4",
  margin: (x: 2.5cm, y: 2.5cm),
  numbering: "1",
)

#set text(
  font: "Liberation Serif",
  size: 11pt,
  lang: "fr",
)

#align(center)[
  #text(size: 22pt, weight: "bold")[Rapport - Projet JEE]
  \ \
  #text(size: 12pt)[Maxime Berger - Valentin Mary]
  \ 
  #text(size: 10pt)[#datetime.today().display("[day]/[month]/[year]")]
]

#v(12pt)

#rect(
  width: 100%,
  stroke: 1pt + rgb("#999999"),
  fill: rgb("#F5F5F5"),
  inset: 10pt,
)[
  #text(weight: "bold", fill: rgb("#555555"))[Connexion au compte de démonstration]
  
  #text( fill: rgb("#666666"))[
    *Identifiant :* `JLM` \
    *Mot de passe :* `JLMpassword`
  ]
]


= Introduction
Dans le cadre du cours d'architecture JEE, nous avons réalisé une application de gestion de club d'escalade, en suivant les différentes préconisations et attendus du sujet, et du cahier des charges fourni.

L'objectif principal de ce projet était de mettre en pratique les différentes notions vues en cours, parmi elles la mise en place d'une architecture en couches avec Spring MVC, l'utilisation de JPA pour la persistance des données, la visualisation de page web avec JSP, et l'utilisation de Spring Security.

L'ensemble des fonctionnalités demandées dans le cahier des charges ont été réalisées, avec une attention particulière portée à l'architecture de l'application. 

= Création du projet
Pour la création de ce projet, nous avons utilisé Spring Initializr, qui nous a permis de générer une structure de projet de base avec les dépendances nécessaires pour notre application. Nous avons choisi les dépendances suivantes :

- Spring MVC/Tomcat : pour la mise en place de l'architecture en couches et la gestion des requêtes HTTP.
- Spring Data JPA : pour la gestion de la persistance des données avec une base de données relationnelle.
- JSP/Servlet : pour la création des pages web et la visualisation des données.
- Spring Security : pour la gestion de l'authentification et des autorisations des membres.
- H2 Database : pour la base de données en mémoire utilisée pendant le développement et les tests, mais également aux fins de la démonstration.
- Maven : pour la gestion des dépendances et la construction du projet.
- JUnit : pour la réalisation des tests unitaires et d'intégration.
- Lombok : pour réduire le code boilerplate et améliorer la lisibilité du code, ainsi que pour les logs avec Slf4j.
- Mockito : pour la réalisation des tests unitaires en simulant les dépendances (nottament les repositories/DTO).
- Spring Boot Starter Mail : pour l'envoi de l'email de réinitialisation de mot de passe.

= Organisation du travail/dépot de code
Nous avons utilisé Git pour la gestion de version de notre projet, avec une branche principale "main" et des branches de développement pour chaque fonctionnalité. Nous avons également utilisé GitHub pour héberger notre code et faciliter la collaboration.

Une pipeline de test a été mise en place sur GitHub Actions, qui s'exécute à chaque push sur la branche "main" et "develop", ainsi qu'au niveau des Pull Request. Nous y reviendrons dans la partie "Tests et CI/CD" de ce rapport.

= Architecture de l'application
L'architecture de notre application est basée sur le modèle MVC (Model-View-Controller), avec pour objectif de séparer les responsabilités des différents modules/couches de l'application.

Chaque couche suit la nomenclature suivante, associée à un package dédié: 
- Configuration: Contient des classes permettant de récupérer les informations de configuration tel que l'encoder de mot de passe, ou l'url de l'application.
- Controller: Contient les classes qui gèrent les requêtes HTTP et les réponses, en interagissant avec les services pour récupérer les données nécessaires à la visualisation des pages web.
- DAO: Contient les classes qui gèrent la persistance des données, en utilisant JPA pour interagir avec la base de données. Ce package aurait pu être nommé Repository, mais nous avons choisi de le nommer DAO pour respecter la nomenclature utilisée dans le sujet.
- DTO: Contient les classes qui représentent les données transférées depuis ou vers l'extérieur de l'application, notamment les données reçues depuis les formulaires web, ou les données envoyées vers les pages web pour la visualisation.
- Model: Contient les classes qui constituent les entités JPA, qui sont utilisées pour la persistance des données dans la base de données.
- Security: Contient la configuration de Spring Security, ainsi que les classes liées à la gestion de l'authentification et des autorisations des membres (notamment dans le sous package validators).
- Service: Contient les classes qui interagissent avec les DAO pour récupérer/enregistrer/préparer les données nécessaires à la réalisation des différentes fonctionnalités de l'application.
- Spécification: Contient les classes qui définissent les spécifications utilisées pour la réalisation de requêtes complexes avec JPA, dans notre cas la requête de recherche d'une sortie avec différents critères de recherche.

On pourra aussi retrouver hors du package java les packages suivants :
- Ressources: Contient le fichier application.properties, avec les différentes propriétés de configuration de l'application, notamment les informations de connexion à la base de données, ou les informations de configuration de JSP.
- Webapp: Contient les fichiers JSP utilisés pour la visualisation des pages web de l'application, avec les différents écrans et composants.

Enfin les tests sont organisés de la même manière que les classes de production, dans le package test.

= Packages et classes
Rentrons à présent dans les détails de l'implémentation de notre application, en présentant les différentes classes qui composent les packages cités précédemment.

== Configuration
 
Dans le package de configuration, on retrouve les classes suivantes :
- PasswordEncoderConfig : Cette classe est annotée avec `@Configuration`, et permet de définir un bean de type PasswordEncoder, qui est utilisé par Spring Security pour encoder les mots de passe des membres. Nous avons choisi d'utiliser l'algorithme BCrypt, qui est considéré comme sécurisé pour le stockage des mots de passe.
- UrlConfig : Cette classe est annotée avec `@Component`, et est constituée d'une propriété "baseUrl" qui est injectée depuis le fichier application.properties. Cette classe est utilisée pour centraliser l'url de base de l'application, qui est utilisée notamment dans les emails de réinitialisation de mot de passe.

== Controller
Dans le package de controller, on retrouve les classes de gestion des routes HTTP. Celle-ci implémente l'arbre de navigation suivant:
```txt
/ (Redirection vers /home)
├── /home (GET)
├── /categories (GET)
│   └── {id} (GET avec paramètres de recherche)
├── /outings
│   └── {id} (GET)
│   └── new (GET et POST)
│   └── update (GET et POST)
│   └── delete (DELETE)
└── /auth
    ├── /login (GET et POST avec Spring Security) 
    ├── /forget-password (GET et POST)
    └── /change-password (GET et POST)
```

En conséquence, on retrouve les classes suivantes :
- HomeController : Cette classe gère la route "/home", qui correspond à la page d'accueil de l'application, avec une présentation du club d'escalade, et des différentes fonctionnalités proposées par l'application (page statique).
- CategoryController : Cette classe gère la route "/categories" et ses sous routes, qui correspondent aux pages de visualisation des différentes catégories de sorties d'escalade (avec la possibilité de rechercher une catégorie par nom), et de visualisation des différentes sorties d'escalade associées à une catégorie sélectionnée, avec la possibilité de rechercher une sortie par différents critères de recherche (Nom, date, organisateur si connecté, etc...).
- OutingController : Cette classe gère les routes "/outings", qui correspondent à la page de visualisation avancée des différentes sorties d'escalade en fonction d'un id sélectionnée, mais également au route de création/modification des sorties de l'utilisateur connecté.
- AuthController : Cette classe gère les routes "/auth", qui correspondent à la page de connexion, de demande de réinitialisation de mot de passe, et de changement de mot de passe. La route de connexion est gérée par Spring Security, tandis que les routes de réinitialisation et de changement de mot de passe sont gérées par des méthodes spécifiques dans cette classe.

== Gestion pages JSP

L'interface utilisateur est décomposée en plusieurs fichiers JSP, permettant une séparation claire entre les éléments de structure et les contenus spécifiques.
=== Fragments de structure
- header : Cette page contient les métadonnées, les liens vers les fichiers CSS et les scripts JavaScript.
- navbar : Cette page contient la barre de navigation dynamique qui adapte ses liens selon que l'utilisateur est un simple visiteur ou un membre connecté.
- footer : Cette page contient le pied de page standardisé et affiché sur l'ensemble du site.

=== Écrans de consultation et d'accueil
- homescreen : Cette page contient la page de garde présentant le club et les actualités.
- categoriesScreen : Cette page affiche la liste des catégories d'escalade disponibles pour orienter l'utilisateur.
- outinglist : Cette page affiche les résultats de recherche ou les sorties liées à une catégorie. C'est ici qu'est gérée la pagination.
- outingdetailsScreen : Cette page est une vue détaillée d'une sortie spécifique avec gestion de la confidentialité (masquage des données privées pour les non-membres).

=== Formulaires et Gestion des sorties
- newoutingformScreen : Cette page contient un formulaire utilisé pour la création et la modification d'une sortie. L'affichage s'adapte dynamiquement selon que l'objet reçu est un nouveau record ou une mise à jour.

=== Authentification et Sécurité
- loginscreen : Cette page contient l'interface de connexion sécurisée gérée par Spring Security.
- forgotpasswordScreen : Cette page permet de saisir son email pour recevoir un lien de récupération.
- changepasswordScreen : Cette page contient le formulaire de saisie du nouveau mot de passe, accessible uniquement via un token valide ou une session sécurisée.

=== DAO

Les DAO (Data Access Object) sont l'interface entre l'application et la base de données.
- CategoryDAO : Cette interface gère la persistence des catégories de sortie et est principalement utilisée dans les listes déroulantes lors des création de sorties et contient une méthode permettant la recherche par nom.
- MemberDAO: Cette interface permet de manipuler les données relatives aux membres, elle permet de récupérer un membre à partir de son ID ainsi que la persistence des nouveaux membres lors de l'inscription.
- OutingDAO : Cette interface est le cœur de l'application, elle gère le stockage et la récupération des sorties d'escalade. L'utilisation de `JpaSpecificationExecutor` permet la génération de requête SQL en fonction des critères de recherche.
- PasswordRecoveryTokenDao : Cette interface est liée à la réinitialisation de mot passe, elle permet stocker et supprimer les tokens générés quand utilisateur oublie son mot de passe.

== DTO

L'utilisation de DTO nous permet de ne transmettre à la vue seulement les informations qui lui sont nécessaires et ainsi éviter tout problème de sécurité ou de performances.
Le package se décompose de la manière suivante :
```
DTO
├── categories
│    └── CategoryDTO
├── member
│    └── MemberDTO
├── outings
│    ├── OutingDTO
│    ├── OutingSearchCriteria
│    ├── OutingsListResponseDTO
│    └── OutingUpdateDTO
└── PaginatedResponse
```
- CategoryDTO : Ce DTO contient le nom et l'identifiant des catégories.
- MemberDTO : Ce DTO contient les informations d'un membre (nom, prénom, username).
- OutingDTO : C'est le DTO principal, il contient les informations de la sortie (id, nom, description, website, date) sa catégorie (CategoryDTO) et son organisateur (MemberDTO).
- OutingSearchCriteria : Ce DTO récupère les critères de filtrage saisis par l'utilisateur dans le moteur de recherche et les passe au `Services` pour construire la requête en fonction des spécifications JPA.
- OutingsListResponseDTO : ce DTO est un wrapper utilisé pour l'écran de recherche. Il contient un `PaginatedResponse<OutingDTO>` pour les résultats, une liste de `MemberDTO` alimenter les filtres d'owners et une chaîne de caractères `error` pour renvoyer des messages à l'utilisateur.
- OutingUpdateDTO : Ce DTO est utilisé pour la mise à jour des sorties, il récupère les modifications de l'utilisateur pour modifier les appliquer à la sortie.
- PaginatedResponse : Ce DTO est un conteneur qui englobe la liste des résultats, la page actuelle (taille et numéro de page), le nombre total d'éléments et de pages ainsi que la présence d'une page suivante ou précédente.

== Model

Le package models contient nos entités persistentes, les classes java sont mappées directement aux tables de notre base de données depuis les annotations JPA.
- Category : Cette entité représente les différentes catégories et possède une relation `@OneToMany` vers `Outing`.
- Member : Cette entité stocke les informations d'identité (nom, prénom) et de connexion (username, email, encodedpassword) des utilisateurs et possède une relation `@OneToMany` vers `Outing`.
- Outing : Cette entité contient les attributs d'une sortie (nom, description, date, website) et possède deux clés étrangères : une relation `@ManyToOne` vers `Category` et une autre relation `@ManyToOne` vers `Member`.
- PasswordRecoveryToken : Cette entité est liée à la sécurité, elle contient le token, la date d'expiration et une relation `@OneToOne` vers le `Member` concerné ce qui permet de valider une seule fois et pendant un laps de temps la réinitialisation du mot de passe avant la suppression du token.

== Spring Security - package security

La sécurité de notre application repose sur deux points : la gestion d'accès via SpringSecurity et la validation des droits métiers via le validator.
- SpringSecurity :
Authentification : Nous utilisons un système `Form Login` où SpringSecurity interagit avec notre MemberService pour vérifier les ID dans la base de données.
Autorisation : Nous avons restreint l'accès (URL) à certaines pages aux simples visiteurs (`/outings/new`, `/outings/*/update`, `/outings/*/delete`).
Gestion de session : Une fois connecté, l'utilisateur dispose d'un objet `Principal` qui nous permet de l'identifier dans toute l'application.

- OutingModificationRightsChecker (validator) : Cette classe sert principalement à éviter la répétition de code lors de la vérification de l'ID pour la modification d'une sortie. Elle fait en sorte que seul le créateur de la sortie ne puisse la modifier.

== Service

Les classes du package services sont la couche métier de l'application, elles servent d'intermédiaires entre les contrôleurs et les DAO.
- CategoryService : Ce service permet de récupérer la liste complète des catégories pour le menu de navigation.
- EmailService : Ce service utilise JavaMailSender pour envoyer des mail de réinitialisation de mot de passe.
- MemberService : Ce service sert à la création de nouveaux comptes, la recherche de membre par l'ID et la mise à jour des informations d'un membre.
- OutingService : Ce service sert à la création et mise à jour des sorties  (lie l'organisateur et la catégorie),
                - à la gestion de la suppression,
                - à la recherche via les spécifications JPA pour retourner filtrés et paginés et
                - à la transformation des entités `Outing` vers les record `OutingDTO`.
- PasswordRecoveryTokenService : Ce service gère génère les tokens, définit leur date d'expiration, vérifie leur validité lorsqu'un utilisateur clique sur le lien et supprime ensuite le token.

== Spécification

- OutingSpecification : Cette classe ne contient qu'une seule `withCriteria` qui transforme un record `OutingSearchCriteria` en un objet `Specification`.
Logique de prédicat : L'`ArrayList<Predicate>` permet d'ajouter un critère que s'il est renseigné par l'utilisateur.
Filtrage par listes : Utilisation de l'opérateur SQL `IN` pour pouvoir sélectionner plusieurs critères en même temps.

= Tests, CI et démonstration

== DataInitializers

Pour mettre en place les tests et démonstrations, nous avons mis en place un classe d'initialisation de données `SampleDataInitalizer`

Pour éviter de tout recréer à chaque redémarrage, le service vérifie si les tables sont vides grâce à la méthode `count()` avant de les recréer.

Pour tester la robustesse, nous avons généré un grand jeu de données contenant :
- 50 catégories.
- 200 membres : chaque membre est créé avec un mot de passe et encodé avec le `PasswordEncoder`.
- 20 000 sorties : 100 sorties sont générées pour chaque membre et réparties équitablement dans les différentes catégories. Ce volume important nous a permis de tester l'efficacité de la pagination, la performance des spécifications JPA et la gestion des jointures entre les membres.

== Tests unitaires

Conformément aux consignes du sujet, nous avons fait des tests unitaires couvrant l'intégralité de la couche service et sécurité. Certaines méthodes ne sont pas testées car elles appellent directement le DAO. Nous avons utilisé le framework JUnit 5 ainsi que Mockito pour l'isolation des composants.

=== Mocking

Pour tester nos services sans solliciter la base de données (on évite de ralentir les tests et la dépendance à l'état des données) nous avons, dans les classes `TestOutingModificationRightsChecker` et `TestPasswordRecoveryTokenService` "mocké" les interfaces DAO.

- TestOutingModificationRightsChecker : Cette classe vérifie que le système autorise ou refuse correctement l'accès à la modification d'une sortie en testant plusieurs scénarios : l'utilisateur est le propriétaire (accès autorisé) l'utilisateur n'est pas le propriétaire (accès refusé) et la sortie n'existe pas.
- TestCategoryService : Cette classe valide la récupération et la transformation des catégories, on s'assure que les `Category` sont bien converties en `CategoryDTO` et que les recherches par nom fonctionnent comme prévu.
- TestMemberService : Cette classe vérifie le bon fonctionnement de la chaine Service #sym.arrow.r DAO #sym.arrow.r Base de donnée.
- TestOutingService : Cette classe valide :
  - La création et mise à jour d'une sortie.
  - L'application des règles de confidentialité (données sensibles masquées dans le DTO si l'utilisateur n'est pas connecté.
  - L'intégration correcte avec les spécifications de recherche.
- TestPasswordRecoveryTokenService : Cette classe vérifie l'état du token de récupération de mot de passe à chaque étape (génération, validation, date d'expiration et suppression) et s'assure qu'il ait le bon état à chaque fois. 
- TestDataInitializer : Cette classe supprime dans un premier temps toutes les tables afin de garantir l'exécution des tests dans un état connu et prévisible. Elle recrée ensuite les tables et teste qu'il y ait bien le bon nombre de catégories, membres et sorties. 

== CI

L'une des exigences majeures de ce projet était de garantir la stabilité du code tout au long du cycle de développement. Pour ce faire, nous avons mis en place une chaîne d'Intégration Continue (CI) via GitHub Actions.

=== Pipeline d'Intégration Continue
Nous avons configuré un workflow automatisé (CI Build and Test) qui s'exécute de manière systématique dans les scénarios suivants :
- À chaque push sur les branches main (production) et develop (développement).
- Lors de l'ouverture ou de la mise à jour d'une Pull Request vers ces mêmes branches.

Cela crée une "porte de qualité" : aucune modification ne peut être intégrée si elle casse le build ou si elle fait échouer un test existant.

=== Étapes du Workflow
Le fichier de configuration YAML définit un environnement de build sous Ubuntu et suit les étapes suivantes :
- Préparation de l'environnement : Récupération du code source (checkout) et configuration du JDK 25 (distribution Temurin). L'utilisation d'un cache Maven permet d'optimiser le temps d'exécution en évitant de télécharger les dépendances à chaque passage.
- Compilation et Packaging (mvn clean package) : Cette étape vérifie que le code compile sans erreur et que la structure du projet (ressources, configurations) permet de générer un artefact fonctionnel.
- Exécution des Tests (mvn test) : C'est le cœur de la validation. Toutes les suites de tests sont lancées. Grâce à notre TestDataInitializer annoté `@Profile("test")`, la CI dispose d'une base de données éphémère parfaitement peuplée pour valider le comportement des services dans un environnement identique à celui du développement.

= Points d'amélioration

Bien que l'application réponde à l'ensemble des exigences du cahier des charges, plusieurs axes d'amélioration ont été identifiés :
- Architecture et encapsulation : Renforcement de la couche Service en isolant mieux la transformation des entités vers les DTO ainsi que la délégation de certaines responsabilités des contrôleurs vers des services dédiés ou des façades.
- Validation et sécurité : Ajout de validateurs dans les formulaires pour renforcer la qualité de saisie (ex : longueur minimale de mot de passe).
- Expérience utilisateur : Mettre la recherche de catégories dans des formulaires (liste déroulante peu ergonomique avec beaucoup de catégories).
- Extensions des tests : Couvrir dans un premiers temps la couche web et dans un deuxième temps les nouveaux services à prévoir (cf. architecture et encapsulation)

= Conclusion
La réalisation de cette application de gestion pour le club d'escalade a constitué un exercice complet de développement logiciel moderne. Ce projet nous a permis de mettre en œuvre les compétences acquises pendant les TPs.

L'un des apports majeurs de ce projet a été l'apprentissage de Spring. Nous avons découvert comment structurer un projet en utilisant ce framework. Apprendre à configurer la sécurité avec Spring Security, à gérer la persistance avec Spring Data JPA et à lier le tout à une interface JSP nous a permis de comprendre la puissance d'un environnement industriel. Nous avons appris à ne plus simplement "écrire du code", mais à utiliser un écosystème complet pour répondre à des besoins complexes.

Ce projet a aussi été notre premier véritable terrain de jeu pour mettre en pratique une architecture en couches (Controller, Service, DAO, Model). Nous avons appris l'importance de séparer les responsabilités entre Contrôleurs, Services et DAO. 

En résumé, ce projet a été une expérience à la fois marquante et stimulante qui, quand elle finit par fonctionner donne une réelle satisfaction.