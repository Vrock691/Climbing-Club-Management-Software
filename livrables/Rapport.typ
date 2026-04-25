#set document(
  title: "Rapport",
  author: "Auteur",
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

= Introduction
Dans le cadre du cours d'architecture JEE, nous avons réalisé une application de gestion de club d'escalade, en suivant les différentes préconisation et attendu du sujet, et du cahier des charges fourni.

L'objectif principal de ce projet était de mettre en pratique les différentes notions vues en cours, parmis elle la mise en place d'une architecture en couches avec Spring MVC, l'utilisation de JPA pour la persistance des données, la visualisation de page web avec JSP, et l'utilisation de Spring Security.

L'ensemble des fonctionnalités demandées dans le cahier des charges ont été réalisée, avec une attention particulière portée à l'architecture de l'application. 

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

Chaque couche suis la nomenclature suivante, associé à un package dédié: 
- Configuration: Contient des classes permettant de récuperer les informations de configuration tel que l'encoder de mot de passe, ou l'url de l'application.
- Controller: Contient les classes qui gèrent les requêtes HTTP et les réponses, en interagissant avec les services pour récupérer les données nécessaires à la visualisation des pages web.
- DAO: Contient les classes qui gèrent la persistance des données, en utilisant JPA pour interagir avec la base de données. Ce package aurait pu être nommé Repository, mais nous avons choisi de le nommer DAO pour respecter la nomenclature utilisée dans le sujet.
- DTO: Contient les classes qui représentent les données transférées depuis ou vers l'extérieur de l'application, notamment les données reçues depuis les formulaires web, ou les données envoyées vers les pages web pour la visualisation.
- Model: Contient les classes qui constituent les entités JPA, qui sont utilisées pour la persistance des données dans la base de données.
- Security: Contient la configuration de Spring Security, ainsi que les classes liées à la gestion de l'authentification et des autorisations des membres (nottament dans le sous package validators).
- Service: Contient les classes qui interagissent avec les DAO pour récupérer/enregistrer/préparer les données nécessaires à la réalisation des différentes fonctionnalités de l'application.
- Spécification: Contient les classes qui définissent les spécifications utilisées pour la réalisation de requêtes complexes avec JPA, dans notre cas la requête de recherche d'une sortie avec différents critères de recherche.

On pourra aussi retrouver hors du package java les packages suivants :
- Ressources: Contient le fichier application.properties, avec les différentes propriétés de configuration de l'application, notamment les informations de connexion à la base de données, ou les informations de configuration de JSP.
- Webapp: Contient les fichiers JSP utilisés pour la visualisation des pages web de l'application, avec les différents écrans et composants.

Enfin les tests sont organisés de la même manière que les classes de production, dans le package test.

= Packages et classes
Rentrons à présent dans le détails de l'implémentation de notre application, en présentant les différentes classes qui composent les packages cité précédemment.

== Configuration
 
Dans le package de configuration, on retrouve les classes suivantes :
- PasswordEncoderConfig : Cette classe est annotée avec `@Configuration`, et permet de définir un bean de type PasswordEncoder, qui est utilisé par Spring Security pour encoder les mots de passe des membres. Nous avons choisi d'utiliser l'algorithme BCrypt, qui est considéré comme sécurisé pour le stockage des mots de passe.
- UrlConfig : Cette classe est annotée avec `@Component`, et est consituée d'une propriété "baseUrl" qui est injectée depuis le fichier application.properties. Cette classe est utilisée pour centraliser l'url de base de l'application, qui est utilisée notamment dans les emails de réinitialisation de mot de passe.

== Controller
Dans le package de controller, on retrouve les classes de gestion des routes HTTP. Celle ci implémente l'arbre de navigation suivant:
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
- CategoryController : Cette classe gère la route "/categories" et ses sous routes, qui correspondent aux page de visualisation des différentes catégories de sorties d'escalade (avec la possibilité de rechercher une catégorie par nom), et de visualisation des différentes sorties d'escalade associées à une catégorie sélectionnée, avec la possibilité de rechercher une sortie par différents critères de recherche (Nom, date, organisateur si connecté, etc...).
- OutingController : Cette classe gère les routes "/outings", qui correspondent à la page de visualisation avancée des différentes sorties d'escalade en fonction d'un id sélectionnée, mais également au route de création/modification des sorties de l'utilisateur connecté.
- AuthController : Cette classe gère les routes "/auth", qui correspondent à la page de connexion, de demande de réinitialisation de mot de passe, et de changement de mot de passe. La route de connexion est gérée par Spring Security, tandis que les routes de réinitialisation et de changement de mot de passe sont gérées par des méthodes spécifiques dans cette classe.

= Tests et CI/CD