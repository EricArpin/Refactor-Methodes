# Journal de refactoring[Planet.java](src/main/domain/Planet.java)

| Classe/méthode | Problème observé | Refactoring appliqué | Justification |
|Cargo/getDescription|Commentaire inutile|Retire|Alourdit inutilement|
|PermissionService|canCarryHazardous() est tres lourde|Utilise bitwise|bcp plus imple a lire|
|PermissionService|cannotCrossRestrictedSector|negation ds le titre de methode|modifie titre pour can... et inverser test pour !=|permet de ne pas jongler avec double negation
|ShipmentService/validateCalculatePrintSaveAndNotify|Titre fait trop d'actions|Renomme applyShipment|---|
|ShipmentService/validateCalculatePrintSaveAndNotify|Mal structuree|Simplifiee ifs, decoupee en sous-methodes|---|
|ShipmentService/validateCalculatePrintSaveAndNotify|Appel de princingSevice.calculatePrice a trop d'arguments|N'envoie que shipment pour regrouper tout|---|
|PricingService|Nb magiques hard codes|Creation de const et d'un Set (Apprentissage AI)|Facilite de localiser les valeurs a modif en cas de changement. Facilite comprendre ce dont il s'agit ds le code|
|PricingService/calculatePrice|Trop d'arguments|refuit a Shipment shipment|---|
PricingService/calculatePrice|Confus|Ajout de sous-methodes, modif des sous methodes avec titres clairs|---|
PricingService/calculateInsurnce|Nom var confus ds la methode|renomme var pour insuranceCost = plus comprehensible|---|
PricingService|Methodes inutilisees|Retirees|Alleger code---|
Shipment/Ajout de 3 methodes/La validation et l'obtention d'infos directement depuis la methode a qui elles appartiennent en lien avec pricingService et ShipmentService
Shipment/Retrait de lignes de codes et var inutulises
Costumer/ Ajout de 2 methodes de validation et des const necessaires.? Assure le traitement dans la bonne classe vs pricingService
Main/ Simplification autour du System.out.println(service.validateCalculatePrintSaveAndNotify(shipment)) qui faisait trop de choses dans le meme titre. Adaptation aux modifs apportees a Shipment
|Main | Trop complexe comme point d'entree | Creation de ShipmentDemoRunner qui active tout ce que main faisait et creation de l'appel a la nouvelle classe dans Main
|Customer/getId + setId| Inutilises| Retire 2 methodes| Alleger code
|ShipmentService/validateShipment| Messages d'erreurs non-recuperes (possiblement pour fonctionnalites futures aver const [ajoutees par moi])| Retrait des const et messages erreurs | Bouts de codes inutilises peuvent etre confondants. ex je pense qu'ils sont utilises qq part.
|ShipmentService/validateShipment | Ne retourne plus de string (car elle remplace les if qui retournaient une String) | change de nom pour isValidShipment, return null si neg, ajustement des returns et transformee en Boolean.
|ShipmentDemoRunner/run| absence de println(bug) |ajout String confirmationMessage pour applyShipment et de println| Sinon programme ne fonctionne plus
|Shipment Service/ApplyShipment vs setCategory| Methode faisait 2 choses - set category et build de message de confirnmation.| Creation de getConfirmationMessage appelant setCategory| Plus clair
|Customer| import inutilise | retrait
|Customer| var id jamais appelee | retrait complet
|ShipmentDemoRunner/builSampleShipment| var id demande en argument mais retire de Customer | Creait un bug vue l'incoherenece des arguments
|ShipmentDemoRunner| import inutilise | retrait|
|ShipmentService | 2 import inutiles | retrait
|ShipmentDemoRunning| Place ds mauvais dossier | Replacer ds dossier App | logique
|Shipment| var totalValue titre pas assez clair et coherent | modif pour totalDeclaredValue | clarte
|Shipment/getTotal/getStatus | methodes inutilisees | retrait
|Shipment/setTotal| nom incoherent avec nouveau nom de variable| Modifie pour setTotalDeclaredValue | coherence
|Cargo/getDescription| methode inutilisee | retrait
|Planet/getName| methode inutilisee | retrait
|Ship/getRegistration+getModel| 2 methodes inutilisees | retrait
|InsuficientFundsException| Classe inutilisee | Retrait
|AccountService| Classe inutilisee | retrait
|ManifestRepositery/count| Methode inutilisee | Retrait
|PermissionService| 3 methodes inutilisees | retrait
|Permission | const CROSS_RESTRICTED_SECTOR | rendue inutilisee | retrait
|RiskService| classe inutilisee | retrait
|ScheduleService| classe inutilisee | retrait