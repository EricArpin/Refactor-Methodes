# Journal de refactoring

| Classe/méthode | Problème observé | Refactoring appliqué | Justification |
|Cargo/getDescription|Commentaire inutile|Retire|Alourdit inutilement|
|PermissionService|canCarryHazardous() est tres lourde|Utilise bitwise|bcp plus imple a lire|
|PermissionService|cannotCrossRestrictedSector|negation ds le titre de methode|modifie titre pour can... et inverser test pour !=|permet de ne pas jongler avec double negation
|---|---|---|---|
|---|---|---|---|
|---|---|---|---|

