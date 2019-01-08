# Tiny Twitter
# Par Raphaël Pagé, Henri Bouvet, Alexandre Melo, et Glenn Plouhinec

https://tinitouit.appspot.com/  
https://tinitouit.appspot.com/_ah/api/explorer

#BACK END 

Pour ce projet nous avons créé 2 beans correspondant aux entités principales: Twitt et User. Elles définissent respectivement les messages publiés par un utilisateur, et les utilisateurs eux-mêmes. 

- Un Twitt possède un id, l'identifiant de l'auteur du twitt, le nom de l'auteur , le message du twitt, la date de creation du twitt et une liste de hashtags. Les hashtags n'ont pas été implémentés dans notre projet, par faute de temps. On peut néanmoins supposer que le processus de recherche de Hashtags est semblable à celui de recherche des Twitts.

- La class User est implémentée de façon à ce qu'un utilisateur de l'application Tiny Twitter ait un identifiant, un login, un email, un mot de passse , un prénom, un nom, et une liste de followers.
Notre méthode de connexion sur l'application s'effectue grâce au login, sans avoir à renseigner le mot de passe, par soucis de simplicité, afin de vérifier et tester les méthodes de notre API plus facilement.

Le fonctionnement des méthodes de l'API aurait été similaire dans le sens où on aurait juste vérifié que l'utilisateur soit bien identifié avant de pouvoir effectuer une requête.
Nous n'avons pas utilisé Objectify car nous n'avons pas réussi à faire fonctionner l'API avec cette bibliothèque, mais son utilisation aurait été fort pratique car cela nous aurait évité de devenir chauve à force de nous arracher les cheveux, et nous aurait économisé bon nombre de lignes de code.


#FRONT END

Pour la methode init dans le fichier tinytwitt.js, un camarade de l'année dernière nous à aidé à l'écrire puisque nous n'avions pas réussi à la faire fonctionner.
 
Mesures de temps moyen (30 mesures) pour l'exécution d'une série d'opérations :
(On ne prend pas la dernière valeur des mesures, car trop aberrante...)

 - Post d'un tweet par une personne suivie par 100 personnes : 74 ms (variance : 14300 ms)
 - Post d'un tweet par une personne suivie par 1000 personnes : 120 ms (variance : 23978 ms)
 - Post d'un tweet par une personne suivie par 5000 personnes :  94.86 ms (variance : 20925 ms)
 
On note d'ailleurs que le premier appel est plus long que les autres, mais il peut être encore plus long si on a fait des requêtes juste avant, ce qui fausse un peu les résultats.
  
 * Récupérer les 10 derniers messages d'une personne suivie par 100 personnes : 120.66 ms (variance : 2954 ms)
 * Récupérer les 50 derniers messages d'une personne suivie par 100 personnes : 166 ms (variance : 2614 ms)
 * Récupérer les 100 derniers messages d'une personne suivie par 100 personnes : 170.3 ms (variance : 2412 ms)
 
 - Récupérer les 10 derniers messages d'une personne suivie par 1000 personnes : 126.13 ms (variance : 5974 ms)
 - Récupérer les 50 derniers messages d'une personne suivie par 1000 personnes : 173.6 ms (variance : 5625 ms)
 - Récupérer les 100 derniers messages d'une personne suivie par 1000 personnes : 198.7 ms (variance : 8242 ms)
 
 * Récupérer les 10 derniers messages d'une personne suivie par 5000 personnes : 102.56 ms (variance : 380.5 ms)
 * Récupérer les 50 derniers messages d'une personne suivie par 5000 personnes : 111.16 ms (variance : 674 ms)
 * Récupérer les 100 derniers messages d'une personne suivie par 5000 personnes : 134.8 ms (variance : 731 ms)
 
 - Récupérer les 50 derniers messages d'un hashtag concernant 1000 messages : XX ms (variance : XX ms)
 - Récupérer les 50 derniers messages d'un hashtag concernant 5000 messages : XX ms (variance : XX ms)
