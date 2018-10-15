

# EMIND1G1 : Fondements de la recherche d'information

## Jesús Lovón Melgarejo

### TP3

#### Q1 
> On trouve les résultats dans les positions suivantes:
- 12. Mario Gómez
- 25. Thomas Müller
> La page *FC Bayern Munich* n'est pas trouvé dans les premières 50 résultats.

#### Q2
> Selon les résultats ci-dessous on trouve la page *Mario Gomez* entre la position 12-13 pour BM25 et BM11 Similarity mais on ne trouve pas la page *FC Batern Munich*. Dans le cas contraire, avec les scores TFIDF et TFIDF (Log et BIR Smooth) on trouve la page du FC mais pas *Thomas Muller*, en plus il y a moins de pertinence pour ces fichiers. Finalement avec TFIDF (TF Total et BIR Smooth) on ne trouve aucune résultat pertinent.
- *BM25*:
- 12. Mario Gómez
- 25. Thomas Müller
- *BM11*:
- 13. Mario Gómez
- 30. Thomas Müller  
- *TFIDF*:
- 21. Mario Gómez
- 34. F.C. Bayern Munich
- *TF Log IDF BIR Smooth*:
- 25. Mario Gómez
- 40. F.C. Bayern Munich
- *TF Total IDF BIR Smooth*:

#### Q3
> Les résultats avec chaque terme de la requête et l'utilisation de BooleanQuery sont les suivants:

- *Boolean Query params: Thomas (MUST) rest of the query (SHOULD)*
- 4. Thomas Müller
- 30. FC Bayern Munich

- *Boolean Query params: Mario (MUST) rest of the query (SHOULD*
- 2. Mario Gómez
- 31. FC Bayern Munich 

> Avec le BooleanQuery on trouve des résultats avec une pertinence qui satisfait presque tous nos besoins. Mais c'est pas totalement parfait car il trouve 2 sur 3 résultats pertinents, en plus, dans les deux cas le résultat "FC Bayern Munich" a un bas pertinence.

> Finalement avec un BooleanQuery plus complexe ( "Munich" AND rest_query) OR ("Mario" AND rest_query) OR ("Thomas" AND rest_query). On trouve:
- 3. Mario Gómez
- 11. Thomas Müller
- 42. FC Bayern Munich

#### Q4
> Avec le BooleanQuery les résultats ne sont pas montrés parmis les premiers 50. Cependant avec un query simple pour chaque term on trouve les ces résultats:

- *Query : Thomas*
- 25. Thomas Müller

- *Query: Mario*
-42. Mario Gómez

- *Query: Munich*
- 10. F.C. Bayern Munich

> Le champ titre n'est pas une bonne ressource pour faire ce type de requête car il n'y a pas assez mot (terms) pour contextualiser requête plus complexes.

#### Q5
> Pour l'implémentation on a choisi la même logique pour le meilleur résultat du Q3. C'est à dire, on pris chaque terme de la requête et on fait un Boolean Query,en même temps on a pris tous ces BooleanQuery pour finalement les ajouter à un plus grande BooleanQuery qui analysera chaque terme pour donner un résultat ensemble parmi tous les terms.

> Pour le tester il faut executer le Main de TPR1 et ensuite taper une requête, les 50 premiers résultats seront montrés.

> En suite les résultats de quelques requêtes:

##### Requête 1:
![Requête 1](https://github.com/jeslev/UPS-FRI/tree/TP3/screenshots/requete1.png "Requête 1")

##### Requête 2:
![Requête 2](https://github.com/jeslev/UPS-FRI/tree/TP3/screenshots/requete2.png "Requête 2")

##### Requête 3:
![Requête 3](https://github.com/jeslev/UPS-FRI/tree/TP3/screenshots/requete3.png "Requête 3")