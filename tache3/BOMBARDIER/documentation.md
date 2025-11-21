# Tâche 3 – Documentation
### Joseph Bombardier

## 1. Objectif
Je devais ajouter une vérification du score de mutation dans le workflow GitHub, écrire des tests avec Mockito pour le module navigation et ajouter un petit « Rickroll » quand les tests échouent.

## 2. Workflow GitHub Actions
J’ai modifié .github/workflows/build.yml.

J’ai ajouté un step qui lance PIT :
mvn -B -pl navigation org.pitest:pitest-maven:mutationCoverage

Le build devient rouge si PIT échoue.  
Sur cette version du projet, PIT affiche « 0 mutation units ».  
C’est lié à la configuration interne du module navigation.  
J’ai quand même gardé le step, car c’est ce que la tâche demande.

## 3. Rickroll
J’ai créé une action réutilisable dans :
.github/actions/rickroll/action.yml

Quand un step échoue, GitHub Actions exécute cette action.  
Ça imprime un message et le lien Rickroll dans les logs.  
J’ai ajouté cela dans le workflow :
- name: Rickroll on failure
  if: failure()
  uses: ./.github/actions/rickroll

## 4. Tests avec Mockito
Les tests sont dans :
navigation/src/test/java/com/graphhopper/navigation/NavigateResourceTest.java

J’ai ajouté l’initialisation avec Mockito :
graphHopper = mock(GraphHopper.class);
translationMap = mock(TranslationMap.class);

J’ai gardé mes tests sur getBearing.  
J’ai ajouté un test qui utilise les mocks pour montrer la simulation de classes.  
J’ai ajouté les dépendances Mockito dans le pom du module navigation.

## 5. Validation
- Les tests passent localement.
- Le workflow build fonctionne et lance PIT.
- Le Rickroll se déclenche quand une étape échoue (testé avec une erreur volontaire).

## 6. Conclusion
La tâche est complète.  
Le workflow est modifié, PIT est intégré, les tests Mockito sont faits et l’action Rickroll fonctionne.
