# REST Service + test

## Dépendences pour ce projet
JDK 17 mais doit fonctionner avec un JDK plus ancien >11

* Spring Web
* Spring JPA Data
* Spring Boot DevTools
* MariaDB Driver
* TestContainers + MariaDB
* Lombok

Nécessite une installation de Docker pour les tests avec MariaDB


## Utilisation de Lombok

Lombok permet de créer des Java Beans en en simplifiant l'écriture. Pas besoin d'ajouter les getters et les setters
Ca permet aussi de générer des builder et des constructeurs.
Ici, on découple encore un peu plus. Les objets renvoyés dans l'API ne sont pas les mêmes que les objets rendus persistent
Student est un objet du modèle de l'application. StudentModel est un objet pour la vue (idéalement il devrait s'appeler StudentViewModel)


Les objets de la vue sont immuables (@Value) Ils ont un constructeur qui prend en paramètre un objet qu'ils représentent.

https://projectlombok.org/

Notez que cette façon de faire se rapproche beaucoup d'une évolution de Java récente (java 14), les records

https://www.baeldung.com/java-record-keyword

## Utilisation de MVCRestTest

https://dzone.com/articles/rest-endpoint-testing-with-mockmvc

## Utilisation de Docker et TestContainer

* Qu'est ce que Docker ? https://www.redhat.com/fr/topics/containers/what-is-docker
* Qu'est ce que TestContainer ?
Test Container permet de faire en sorte que des tests utilise des containers pour s'exécuter. Ici on utilise un container incluant une isntance de MariaDB pour exécuter certains tests dans une vraie base de données.
https://www.testcontainers.org/

## Test manuel des Controller REST
Vous pouvez aussi tester manuellement les contrôleurs REST en utilisant un client HTTP
Un client est accessible dans IntelliJ dans le menu Tools>HTTP Client

Pour les tests manuels, vous devrez démarrer une base de données Maria DB. Vous pouvez utiliser Docker pour ça aussi, ce qui évite une installation supplémentaire.
Dans le fichier resource/application.properties vous trouverez les info de configuration pour la connection à cette Datasource



## A faire pour ce TP
* Installer Docker si ce ce n'est pas disponible sur votre machine
* Pour démarrer une image docker configurée avec mariadb exécuter docker-compose.yml 
  * Soit par un menu contextuel du fichier dans IntelliJ (Crtl-Maj-F10)
  * Soit en ligne de commande : docker-compose -f docker-compose.yml up
* Exécuter les tests avec maven
  * mvn test (à la racine du projet)
  * Dans IntelliJ, démarrer les tests depuis la fenêtre Maven
* Tester le client HTTP pour faire des requêtes directement sur les API (pensez à la BD)
  * Démarrez l'application (ResttestApplication > Run)
  * utilisez curl ou le HTTPClient de IntelliJ (Tools>HTTP Client)
  * Vous pouvez aussi utiliser la fenêtre Endpoints
  * Faites des requêtes pour accéder aux ressources et les modifier

* Ajouter le StudentController et les méthodes correspondantes, en particulier celles pour gérer la relation entre Student et Club et écrivez des tests.
  * Complétez les tests de StudentService
  * Ajoutez une classe de tests pour StudentController sans Mock
  * Ajoutez une classe de tests pour StudentController avec Mock

  Si vous avez des soucis avec la base de données, 
 
