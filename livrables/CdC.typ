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
  #text(size: 22pt, weight: "bold")[Cahier des Charges - Projet JEE]
  \ \
  #text(size: 12pt)[Maxime Berger - Valentin Mary]
  \ 
  #text(size: 10pt)[#datetime.today().display("[day]/[month]/[year]")]
]

= Présentation du projet 

L'objectif est de concevoir et développer une application web robuste pour un club d'escalade. Cette plateforme doit permettre de recenser les sorties en fonction de leurs catégories, de gérer les membres et d'offrir un moteur de recherche performant tout en garantissant la confidentialité des données privées.

= Besoin Fonctionnel

== Gestion des sorties (Outings)

L'application doit se comporter différemment selon la personne qui l'utilise :
- Consultation publique : Tout visiteur doit pouvoir voir la liste des sorties et les détails généraux.
- Création/Modification : Un membre connecté doit pouvoir créer une sortie mais seul l'organisateur (owner) doit avoir le droit de modifier ou supprimer sa propre sortie.
- Catégorisation : Chaque sortie doit être rattachée à une catégorie et un membre (owner).
- Confidentialité : Les informations sensibles (site web, nom de l'organisateur) ne doivent pas être visibles par les visiteurs et membres non authentifié.

== Recherche et Navigation

Utilisation d'un moteur de recherche avancé : Filtrage dynamique par nom, par catégories, par organisateurs ou par plage de dates.
\
Les résultats doivent être paginés coté serveur pour supporter un grand volume de données (20 000+ sorties).

== Gestion de membre

Les membres doivent pouvoir utiliser un certain nombre de fonctionnalités et de manière sécurisée :
- Authentification : Système de connexion sécurisé par identifiant/mot de passe.
- Inscription : Possibilité de créer un compte membre.
- Récupération de mot de passe : Processus sécurisé par envoi d'email et token temporaire en cas d'oubli.
- Profil : Modification des informations personnelles.

#pagebreak()
= Spécifications Techniques

== Technologies utilisé

- Langage : Java 25.
- Framework : Spring Boot 3.
- Moteur de templates : JSP avec JSTL et Spring Form Tags.
- Persistance : Spring Data JPA avec Hibernate.
- Base de données : H2 (Base de données en mémoire pour le développement).
- Sécurité : Spring Security avec encodage BCrypt.

== Architecture logicielle

L'architecture de l'application doit remplir certains critères :
- Utilisation d'un modèle en couches : Séparation stricte entre Contrôleurs, Services, DAO et Modèles.
- Transfert de données : Utilisation systématique de Java Records pour les DTO afin de garantir l'immuabilité.
- Requêtage dynamique : Utilisation de l'API JPA Criteria via les Specifications pour le moteur de recherche.

= Contraintes de Qualité et Developemment

== Robustesse et tests

L'application doit contenir une section de tests couvrant un maximum de son code comprenant des tests unitaires (utilisation de JUnit 5 et Mockito) ainsi que des tests d'intégration (validation du service membre avec la base de donnée)


== CI

Mise en place d'un workflow GitHub Actions pour compiler le projet et exécuter les tests à chaque modification (Push/Pull Request).
\
Gestion des versions : Utilisation de Git avec une stratégie de branches (main, develop).

== Performances

L'application doit rester fluide avec un jeu de données de test massif (20 000 sorties et 200 membres).
