#TP CLERC - LANGLOIS SDIS

##Travaille realisé
Nous avons comence par refactorise le code de l'application pour le rendre plus facile a utiliser.
###Student controler

- Creation d'un Student
- Edition d'un Student
- Suppression d'un Student
- Recherche d'un Student
- Affectation de la presidence d'un club

Cela est accompagne de test repartis dans 4 fichier : 

- 3 de test unitaire : StudenstServiceTest , StudentServiceMockTest et StudentRepositryTest
- 1 de test integration : StudentControllerTest

##Club controler
Nous avons aussi rajout plutire metode au club controler

- Metode pour ajoute un Studenet a un club
- Metode pour supprimer un Studenet d'un club
- Metode pour affecte un president a un club

Ses modification ce son accompagne des test unitaire et dintegration affin de sasure du bon focntiomnet .

Nous avons aussi changez des metode dans le Club modelle affin d'evite une recursin infinus lors de la serialisation des donne
