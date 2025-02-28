# Application Trésor

Cette application permet de faire un jeu de chasse au Trésor.
Le but du jeu est qu'il soit collaboratif avec 2 joueurs où ils 
doivent aller sur des points placés aléatoirement sur une carte.
Soit les 2 joueurs doivent aller en même temps sur ce point, soit
ils doivent être séparés. Lorsque les joueurs arrivent sur un point
un mini-jeu est lancé. Les joueurs peuvent communiquer à tout moment 
avec un chat. Au total, il y a 3 mini-jeux et à la fin, il y a un
décompte du nombre de victoires.

## Les fonctionnalités

En général :
* Connexion / Inscription
* Un menu paramètre
* Un mode pour jouer

Avant le jeu :
* Un menu pour créer une équipe avec génération d'un code
* Un menu pour rejoindre une équipe avec un code à entrer

Pendant le jeu : 
* Une carte de l'IUT avec les placements 2 joueurs actualisés en temps réel
* Un chat toujours accessible
* 3 mini-jeux :
  * Un jeu de devinette où 1 joueur a la question et l'autre plusieurs réponses possibles
  * Un jeu de placement des pions où 1 joueur a 10 couleurs et l'autre doit appuyer sur les couleurs et 
  doit les placer dans l'ordre
  * Un jeu de puzzle où les 2 joueurs doivent faire le puzzle où chaque joueur possède la moitié des pièces

Dans les paramètres :
* Un mode malvoyant où il y a un son qui cite le texte ou fait une description de l'activity
* Un menu pour modifier des informations sur le compte utilisateur

## Les APIs

Cette application utilise 2 APIs :
* Une API pour les requêtes REST SpringBoot utilisant une BDD PostgreSQL
* Une API pour les requêtes WebSocket avec Socket.io sur un serveur Node.js

## Comment y jouer ?

Pour jouer, il faut télécharger l'APK disponible sur GitHub

## Les tests

Robolectric -> test unitaire sur Android sans émulateur <br>
Mockito -> test unitaire avec des mocks (objets simulés) <br>
Espresso -> test UI limité à l'application <br>
UI Automator -> test UI mais pas limité à l'application <br>
Appium -> équivalent de Selenium mais multiplateforme <br>
JUnit -> test fonctionnalité Java