# Tiny Twitter

BACK END 

Pour ce projet nous avons créé 2 class Beans ,Twitt et User. Elles definissent réspectivements les twitt d'un utilisateur en possedant, un Identifiant, l'identifiant de l'auteur du twitt, le nom de l'auteur , le message du twitt, la date de creation du twitt et une liste de hashtag. Les hashtag n'ont pas été implémenté dans notre projet, par faute de temps. Cependant cela revient à faire comme pour la recherche de tweet mais avec les hashtag.
La class User est implementée de facon à ce qu'un utilisateur de tiny twiit est un identifiant, un login, un email, un mot de passse , un prenom, un nom et une liste de followers.
Notre methode de connection ne comprend que l'authentification grace au login, cela est dû au faite que pour effectuer des tests de creation de twitt cela etait plus rapide. Sinon nous aurions rajouté le mot de passe avec. La methode de l'API pour savoir si l'utilisateur existe aurait été la meme en rajoutant en requete le mot de passe en plus. Nous n'utilisons pas objectify , étant donné que nous avions queleque probleme avec. Cepandant nous aurions bien aimé , puisque cet outils est très pratique et réduit grandement le nombre de ligne à écrire.


FRONT END
 Pour la methode init dans le fichier tinytwitt.js, un camarade de l'année derniere nous à aigué pour l'ecrire puisque nous n'avions pas réussi à la faire fonctionner .
Mesures de temps moyen (30 mesures) pour l'exécution d'une série d'opérations :

 - Post d'un tweet par une personne suivie par 100 personnes : XX ms (variance : XX ms)
 - Post d'un tweet par une personne suivie par 1000 personnes : XX ms (variance : XX ms)
 - Post d'un tweet par une personne suivie par 5000 personnes : XX ms (variance : XX ms)  
  
 * Récupérer les 10 derniers messages d'une personne suivie par 100 personnes : XX ms (variance : XX ms)
 * Récupérer les 50 derniers messages d'une personne suivie par 100 personnes : XX ms (variance : XX ms)
 * Récupérer les 100 derniers messages d'une personne suivie par 100 personnes : XX ms (variance : XX ms)
 
 - Récupérer les 10 derniers messages d'une personne suivie par 1000 personnes : XX ms (variance : XX ms)
 - Récupérer les 50 derniers messages d'une personne suivie par 1000 personnes : XX ms (variance : XX ms)
 - Récupérer les 100 derniers messages d'une personne suivie par 1000 personnes : XX ms (variance : XX ms)
 
 * Récupérer les 10 derniers messages d'une personne suivie par 5000 personnes : XX ms (variance : XX ms)
 * Récupérer les 50 derniers messages d'une personne suivie par 5000 personnes : XX ms (variance : XX ms)
 * Récupérer les 100 derniers messages d'une personne suivie par 5000 personnes : XX ms (variance : XX ms)
 
 - Récupérer les 50 derniers messages d'un hashtag concernant 1000 messages : XX ms (variance : XX ms)
 - Récupérer les 50 derniers messages d'un hashtag concernant 5000 messages : XX ms (variance : XX ms)
