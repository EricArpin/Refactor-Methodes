# Défi de refactoring — Galactic Freight

## Mise en situation

Galactic Freight est une application de gestion de transport interstellaire. Elle prépare une expédition entre deux planètes, calcule son prix, vérifie les autorisations du vaisseau, puis enregistre et confirme l'expédition.

L'entreprise a cependant développé son application très rapidement. Le programme fonctionne, mais le code a accumulé des décisions qui rendent les modifications risquées : certaines méthodes sont longues, plusieurs responsabilités sont mélangées, certaines conditions sont difficiles à comprendre et plusieurs interfaces de méthodes sont devenues lourdes.

Votre travail n'est pas d'ajouter de nouvelles fonctionnalités. Vous devez améliorer la structure interne du programme tout en conservant son comportement actuel.

## Objectif du défi

Refactorez le projet afin de le rendre :

- plus facile à lire et à expliquer;
- plus simple à modifier;
- mieux organisé autour de responsabilités claires;
- moins répétitif;
- plus explicite dans ses noms, ses types et ses conditions;
- plus sécuritaire face aux erreurs de programmation.

Un refactoring réussi ne se mesure pas au nombre de lignes supprimées. Il se mesure surtout à la clarté du résultat et à la facilité avec laquelle un prochain développeur pourrait modifier le programme.

> Un refactoring modifie la structure du code, pas ses fonctionnalités ni son comportement observable.


## Travail permis

Vous pouvez :

- renommer des méthodes et variables;
- extraire ou intégrer des méthodes;
- modifier la visibilité;
- éliminer la duplication;
- réduire ou regrouper des paramètres;
- déplacer une responsabilité;
- introduire une classe, une énumération ou une exception;
- simplifier des conditions;
- améliorer l'usage des permissions bitwise;
- rendre des données immuables;
- adapter les tests lorsqu'une API évolue intentionnellement.

Vous ne devez pas :

- ajouter de nouvelles fonctionnalités métier;
- changer arbitrairement les règles de calcul;
- supprimer des validations;
- modifier une valeur attendue
- remplacer tout le projet sans pouvoir justifier les transformations.

## Journal de refactoring

Documentez au moins dix transformations dans `REFACTORING.md`. Pour chacune, indiquez :

1. la classe ou méthode concernée;
2. le problème observé;
3. le refactoring appliqué;
4. la justification;
5. les tests utilisés.

Exemple :

```text
Problème : la méthode X mélange validation et affichage.
Transformation : extraction d'une méthode privée Y.
Justification : chaque méthode possède une responsabilité plus claire.
Vérification : les 27 tests réussissent encore.
```

## Questions à se poser

- Le nom de la méthode explique-t-il son intention?
- Effectue-t-elle une seule tâche?
- Ses paramètres sont-ils tous nécessaires?
- La condition se comprend-elle sans être décodée mentalement?
- Une variable conserve-t-elle toujours la même signification?
- Le type retourné décrit-il clairement le résultat?
- Cette responsabilité appartient-elle à cette classe?
- Cette méthode doit-elle vraiment être publique?
- Les erreurs sont-elles représentées clairement?
- Le nouveau code est-il réellement plus simple?

Il n'existe pas nécessairement une solution unique. Deux étudiants peuvent produire des structures différentes et valides si leurs décisions sont cohérentes et justifiées.

## Fonctionnement général

Le scénario principal se trouve dans `Main.java` :

1. un client demande le transport d'une cargaison;
2. une planète d'origine et une destination définissent la route;
3. un vaisseau est assigné à l'expédition;
4. plusieurs cargaisons peuvent être ajoutées au manifeste;
5. `ShipmentService` valide et traite l'expédition;
6. `PricingService` calcule le transport et l'assurance;
7. `PermissionService` vérifie les autorisations du vaisseau;
8. l'expédition est enregistrée;
9. une confirmation textuelle est produite.

Le résultat normal ressemble à ceci :

```text
PRIORITY | GF-2026-001 | 2735.40 | CONFIRMATION GF-2026-001 -> Nova Trading
```

Cette ligne indique la catégorie, la référence, le prix final et la confirmation envoyée au client.

## Modèle du domaine

### `Customer`

Représente le client : identifiant, nom, années de fidélité, états actif et suspendu, ainsi que le solde du compte. Son état influence son admissibilité et certains calculs.

### `Planet`

Représente une origine ou une destination. Chaque planète possède un nom, un secteur et un niveau de sécurité. Le secteur et la sécurité peuvent augmenter le prix ou le risque de la route.

### `Cargo`

Représente un élément transporté avec une description, un poids, une valeur déclarée et une indication de dangerosité. Le poids et la valeur de toutes les cargaisons sont additionnés pendant le traitement.

### `Ship`

Représente le vaisseau assigné : numéro d'enregistrement, modèle, capacité maximale et permissions. La capacité doit être suffisante pour le poids total.

### `Shipment`

Relie le client, l'origine, la destination, le vaisseau, la date de départ et les cargaisons. Son état commence à `CREATED`. Après un traitement valide, son prix est enregistré et son état devient `READY`.

## Services

### `ShipmentService`

Cette classe orchestre le traitement principal. Elle vérifie notamment :

- que le client est actif et non suspendu;
- que l'expédition contient une cargaison;
- que le poids respecte la capacité du vaisseau;
- que le vaisseau peut transporter une cargaison dangereuse.

Après validation, elle calcule le total, modifie l'état, enregistre l'expédition et produit une confirmation.

Les échecs sont actuellement représentés par :

```text
ERROR_CUSTOMER
ERROR_EMPTY
ERROR_CAPACITY
ERROR_PERMISSION
```

Ces résultats font partie du comportement protégé par les tests. Si votre refactoring fait évoluer la gestion des erreurs, adaptez les tests de façon réfléchie.

### `PricingService`

Calcule les éléments du prix :

- tarif selon le poids;
- supplément selon la valeur déclarée;
- supplément de dangerosité;
- supplément selon la sécurité des planètes;
- supplément pour changement de secteur;
- supplément saisonnier;
- rabais de fidélité;
- assurance.

Les nombres présents représentent les règles métier actuelles. Vous pouvez restructurer leur implantation, mais les mêmes données doivent produire les mêmes résultats.

### `PermissionService`

Gère les permissions du vaisseau. Elles sont enregistrées dans un seul entier à l'aide de bits :

| Permission | Valeur |
|---|---:|
| Voir le manifeste | `1 << 0` |
| Modifier le manifeste | `1 << 1` |
| Transporter des matières dangereuses | `1 << 2` |
| Traverser un secteur restreint | `1 << 3` |

Plusieurs permissions peuvent être combinées avec `|` :

```java
int permissions = Permissions.VIEW_MANIFEST
        | Permissions.EDIT_MANIFEST
        | Permissions.CARRY_HAZARDOUS;
```

Une permission peut être recherchée avec `&` :

```java
boolean authorized =
        (permissions & Permissions.CARRY_HAZARDOUS) != 0;
```

Le projet contient volontairement différentes façons de manipuler ces permissions. Déterminez lesquelles expriment le mieux l'intention.

### `ScheduleService`

Gère les jours de départ. Un masque binaire conserve plusieurs jours dans un entier, chaque bit correspondant à une journée. Lundi, mercredi et vendredi produisent la valeur `21` (`1 + 4 + 16`). Le service fournit aussi le nombre de départs d'une journée donnée.

### `AccountService`

Gère certaines opérations du compte client, notamment le retrait d'un montant et les points de récompense.

### `RiskService`

Évalue le risque selon la route, la sécurité de la destination, la présence de cargaisons dangereuses et leur valeur.

### `ManifestRepository` et `NotificationService`

Le premier simule l'enregistrement en mémoire; le second produit la confirmation. Il n'y a ni base de données ni véritable messagerie : le défi porte sur les méthodes, pas sur l'infrastructure.

## Organisation du projet

```text
projet/
├── src/main/
│   ├── app/          Point d'entrée
│   ├── domain/       Objets du domaine
│   ├── exception/    Exceptions
│   └── service/      Règles métier
├── REFACTORING.md    Journal à compléter
```
