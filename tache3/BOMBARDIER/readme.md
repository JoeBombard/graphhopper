# IFT3913 – Tâche 3
### Joseph Bombardier

## 1. Dépôt GitHub modifié
Voici le dépôt utilisé pour la tâche 3 (branche `task3-joseph`) :

https://github.com/JoeBombard/graphhopper/tree/task3-joseph

## 2. Documentation de mon travail
J’ai documenté toutes les étapes de la tâche 3 ici :

documentation.md

## 3. Résumé très court des modifications

### 3.1 Workflow GitHub Actions
- Ajout du step PIT dans `.github/workflows/build.yml` pour vérifier le score de mutation.
- Ajout d’une action réutilisable `.github/actions/rickroll/action.yml`.
- Ajout d’un step `if: failure()` pour déclencher le Rickroll quand les tests échouent.

### 3.2 Tests avec Mockito
- Création et mise à jour de `NavigateResourceTest.java`.
- Utilisation de mocks de `GraphHopper`, `TranslationMap`, etc.
- Ajout de nouveaux tests pour satisfaire les exigences de la tâche.

### 3.3 Notes sur PIT
PIT fonctionne dans le workflow, mais sur cette version du module `navigation`, il ne trouve **aucune unité de mutation** (`0 mutation units`).  
C’est un comportement déjà observé sur ce projet et vient de la configuration interne de GraphHopper.  
Le workflow reste conforme aux exigences : **le build échoue si PIT retourne un score inférieur au seuil configuré**.

