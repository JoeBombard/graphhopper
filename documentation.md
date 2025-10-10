# Documentation des tests – Tâche 2 (IFT3913)

## Binôme
- Joseph Bombardier (20275345)  
- skdon134

---

## Objectif
Cette tâche consistait à améliorer la qualité des tests unitaires dans le module `navigation` du projet GraphHopper.  
On devait :
- Sélectionner jusqu’à 3 classes avec une couverture de mutation partielle  
- Ajouter de nouveaux tests unitaires pertinents  
- Inclure au moins un test utilisant **Java Faker**  
- Exécuter PIT pour comparer les scores avant/après  
- Documenter les intentions et résultats de nos tests  

---

## Classes testées
Les trois classes choisies sont :

1. **DistanceConfig**  
   Gère la configuration des instructions vocales selon les unités (métriques/impériales) et le mode de transport.

2. **NavigateResource**  
   Contient les endpoints de l’API de navigation (GET/POST) et la logique de validation des requêtes.

3. **VoiceInstructionConfig**  
   Définit la logique pour déterminer quelle instruction vocale prononcer selon la distance restante.

Ces classes avaient déjà des tests partiels, mais plusieurs branches restaient non couvertes, notamment des conditions dans `NavigateResource` et `DistanceConfig`.

---

## Environnement utilisé
- Java 17 (Adoptium)  
- Maven 3.9.11  
- JUnit 5.10.2  
- PIT Mutation Testing 1.20.6  
- Java Faker 1.0.2  

Les tests et rapports PIT ont été générés dans le dossier :
```
navigation/target/pit-reports/index.html
```

---

## Tests ajoutés et justifications

### 1. DistanceConfigTest

**TestConfigImperialCar()**  
- **Intention :** vérifier la création correcte de `DistanceConfig` pour le mode voiture (imperial).  
- **Oracle :** les valeurs internes de distances doivent correspondre aux conversions impériales.  
- **Motivation :** certaines branches du constructeur n’étaient pas couvertes.  
- **Résultat :** tue des mutants sur les conditions `if (unit == METRIC)`.

**TestConfigMetricWalking()**  
- **Intention :** tester le mode piéton en métrique.  
- **Oracle :** la liste des instructions vocales doit être initialisée correctement.  
- **Motivation :** couvrir la branche spécifique au mode “foot”.  
- **Résultat :** mutants “negated conditional” éliminés.

**TestConfigImperialCycling()**  
- **Intention :** valider le mode vélo (imperial).  
- **Oracle :** vérifier que les distances vocales sont cohérentes avec le système impérial.  
- **Motivation :** combler la couverture du `switch` du constructeur.  
- **Résultat :** tue plusieurs mutants dans le bloc BIKE/imperial.

---

### 2. NavigateResourceTest

**TestGetBearingErreurNonNumeric()**  
- **Intention :** tester la robustesse de `getBearing()` quand une valeur n’est pas numérique.  
- **Oracle :** une `IllegalArgumentException` doit être levée.  
- **Motivation :** plusieurs mutants “negated conditional” sur `contains(",")` restaient vivants.  
- **Résultat :** mutants supprimés.

**TestGetBearingParseWithNaN()**  
- **Intention :** vérifier la gestion des chaînes vides (`;;`).  
- **Oracle :** les positions vides deviennent `Double.NaN`.  
- **Motivation :** améliorer la robustesse et la lisibilité des cas limites.  
- **Résultat :** tue un mutant sur la gestion du `split()` et sur le test de vide.

**TestDoGetStepsDesactive()**  
- **Intention :** tester la méthode `doGet()` avec `steps=false`.  
- **Oracle :** doit lever `IllegalArgumentException`.  
- **Motivation :** aucune couverture avant sur cette condition.  
- **Résultat :** mutants “negated conditional” tués.

---

### 3. VoiceInstructionConfigTest

**TestStreetConditionalDistance()**  
- **Intention :** valider la génération de texte dynamique des instructions vocales.  
- **Oracle :** le texte retourné doit inclure le nom de rue et la distance.  
- **Motivation :** tester la concaténation et l’internationalisation.  
- **Outil :** utilise **Java Faker** pour générer un nom de rue aléatoire.  
- **Résultat :** couverture augmentée, mutants sur la concaténation tués.

**TestInitialVICBoundary()**  
- **Intention :** vérifier le comportement autour du seuil `distanceForInitialStayInstruction`.  
- **Oracle :** aucune instruction si distance inférieure, instruction si supérieure.  
- **Motivation :** un mutant modifiant le signe de comparaison avait survécu.  
- **Résultat :** mutant “changed conditional boundary” éliminé.

---

## Résultats PIT

Avant nos ajouts :
- DistanceConfig : 47%
- NavigateResource : 10%
- VoiceInstructionConfig : 97%

Après :
- DistanceConfig : environ 70%
- NavigateResource : environ 45%
- VoiceInstructionConfig : 100%

Global : **59% de mutants tués**  
Le score global ne dépasse pas le seuil de 80%, mais la progression est importante.  
Les tests couvrent maintenant les principales branches logiques et comportements critiques.

---

## Analyse
Les gains les plus notables viennent de `NavigateResource`, où la plupart des conditions d’erreur (`if !steps`, `if !voiceInstructions`, etc.) étaient auparavant sans couverture.  
`DistanceConfig` a aussi bénéficié de tests couvrant plusieurs unités et modes.  
`VoiceInstructionConfig` est maintenant entièrement couvert.

Les survivants restants sont surtout liés à des cas limites de calculs internes ou à des conversions numériques qui ne sont pas testées dans le contexte actuel.

---

## Conclusion
Cette tâche a permis de renforcer la robustesse du module navigation.  
Les nouveaux tests ajoutés augmentent la couverture de mutation et améliorent la détection de bugs potentiels.  
L’utilisation de Java Faker a permis d’introduire un peu de variabilité et de réalisme dans les données testées.  
Le rapport PIT final confirme une progression nette dans la qualité globale des tests.
