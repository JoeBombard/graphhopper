# Tâche 2 - IFT3913

## Binôme
Joseph Bombardier (20275345)  
yassine azmani

---

## Classes testées
Les classes choisies sont :
- DistanceConfig
- NavigateResource
- VoiceInstructionConfig

Elles viennent du module `navigation/src/main/java/com/graphhopper/navigation/`.  
On les a choisies parce qu’elles avaient déjà des tests unitaires mais pas une couverture de mutation complète.

---

## Objectif du travail
L’objectif était d’ajouter des tests unitaires supplémentaires pour améliorer la couverture de mutation, puis de comparer les résultats avant et après avec PIT.

Tâches principales :
- Ajouter des tests (au moins 7)
- Utiliser java-faker dans au moins un test
- Générer les rapports PIT avant et après
- Rédiger un document expliquant quelles mutations ont été détectées ou non

---

## Outils utilisés
- Java 17 (Adoptium)
- Maven 3.9.11
- JUnit 5.10.2
- PIT Mutation Testing 1.20.6
- Java Faker
- AssertJ et Mockito

---

## Tests ajoutés

### TestConfigMetriclWalking
Vérifie le constructeur de `DistanceConfig` quand le mode est WALKING (foot).  
Le but est de s’assurer que la bonne configuration vocale est générée.  
Avant ce test, plusieurs conditions dans le switch n’étaient pas couvertes.

---

### TestConfigImperialCycling
Test du constructeur `DistanceConfig` pour le mode BIKE avec les unités impériales.  
Ce test vérifie que le constructeur gère bien le mode vélo avec les bonnes valeurs.

---

### TestDoGetStepsDesactive
Couvre `NavigateResource.doGet()` quand `steps=false`.  
Avant, cette branche n’était pas testée (mutation “negated conditional”).  
Maintenant, le test s’assure qu’une IllegalArgumentException est levée.

---

### TestGetBearingErreurNonNumeric
Teste la méthode `NavigateResource.getBearing("abc,1")`.  
Vérifie que la fonction lève une exception si une valeur n’est pas numérique.

---

### TestGetBearingParseWithNaN
Teste `NavigateResource.getBearing("100,1;;200,1;")`.  
Vérifie que les champs vides deviennent `Double.NaN` et que les autres valeurs sont bien converties.

---

### TestStreetConditionalDistance
Test sur `ConditionalDistanceVoiceInstructionConfig.getConfigForDistance()`.  
Utilise java-faker pour générer un nom de rue (ex: faker.address().streetName()).  
Vérifie que le texte retourné contient le nom de rue et une distance correcte.  
Ce test permet de valider la logique des instructions vocales.

---

### TestInitialVICBoundary
Teste `InitialVoiceInstructionConfig.getConfigForDistance()` autour du seuil 4250m.  
Vérifie que l’instruction n’est créée que si la distance dépasse le seuil.  
Ce test tue le mutant qui remplaçait “>” par “>=”.

---

## Résultats PIT

### Avant
- DistanceConfig : 47%
- NavigateResource : 10%
- VoiceInstructionConfig : 97%
Score global : environ 47%

Mutants survivants :
- plusieurs “negated conditional” dans NavigateResource
- un “boundary conditional” dans VoiceInstructionConfig ligne 130

---

### Après
- DistanceConfig : ~70%
- NavigateResource : ~45%
- VoiceInstructionConfig : 100%
Score global : 59%

Les principaux mutants ont été tués :
- NavigateResource : conditions “steps=false” et “X-GH-Took”
- DistanceConfig : modes FOOT et BIKE testés
- VoiceInstructionConfig : condition “>” corrigée

---

## Résumé des améliorations
Classe | Avant | Après
-------|--------|------
DistanceConfig | 47% | ~70%
NavigateResource | 10% | ~45%
VoiceInstructionConfig | 97% | 100%

---

## Conclusion
Les nouveaux tests ont permis d’augmenter significativement la couverture de mutation, en particulier sur NavigateResource et DistanceConfig.  
Tous les tests passent localement et le rapport PIT montre qu’on a éliminé plusieurs mutants critiques.  
Un test avec java-faker a aussi été ajouté pour valider la génération dynamique de texte d’instructions vocales.  
En résumé, la suite de tests couvre mieux les comportements principaux du module navigation.
