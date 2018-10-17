# EMIND1G1 : Fondements de la recherche d'information

## Jesús Lovón Melgarejo

### TP5

#### Q1 
> On peut trouver le résultat dans le fichier 10noSim.txt
> 
|  |    | |
| ------ | --- | --- |
| num_q    |      	all |	14  |
| num_ret  |      	all |	140 |
|  num_rel  |      	all |	15 |
| num_rel_ret |   	all |	9 |
| map        |    	all |	0.3353 |

> On verifie qu'il y a 14 queries (num_q) avec 140 résultats en total (num_ret) parmi lesquels il y a 15 résultats pertinents (num_rel) mais on a trouvé seulement 9 (num_rel_ret). Ca donne une précision moyenne 0.3353 (MAP).

#### Q2
> Le fichier qrel.tp5 se trouve dans le repositoire

#### Q3
> Pour chaque requête on a pris les 100 premiers résultats, ayant en total 14 terms, il donne 1240 résultats pour les évaluer. Les critères suivantes étaient utilisées :  BM11 Similarity, BM15 Similarity, BM25 Similarity, TF Log+IDF Smooth Similarity et TF Total+IDF Smooth Similarity.
> En analysant les champs num_rel , num_rel_ret et map on peut trier les configurations plus performantes comme : BM25 > BM11 > TF Total/ IDF Smooth > BM15 > TF Log / IDF Smooth. Avec les précisions (MAP) :  0.1601 < 0.2030 < 0.2516 < 0.3138 < 0.3391

>La principal raison pour les différents résultats est le calcul de score de chaque méthode. Il est possible que le score de BM15 soit plus faible que BM25 pour un résultat donné alors, la comparaison avec QREL est plus distante. 

#### Q4
> Avec les mêmes caractéristiques de Q3, mais cette fois on prends les 1000 premiers résultats:
> Cette fois les configurations plus performantes sont : BM25 > BM11 > TF Total/ IDF Smooth > BM15 > TF Log / IDF Smooth. Avec les précisions (MAP) :  0.1622 > 0.2034 > 0.2533 > 0.3151 > 0.3402

> On voie que les champs num_q  , num_ret et num_rel   restent les mêmes et ce qui change c'est le champ num_rel_ret.  Il est logique de voir ce résultat étant donné que c'est la même requête et criteria, on a changé seulement les résultats à considérer. Donc le nombre des documents s'eleve et de même façon les autres metrics de précision. 
> Il faut faire attention aux quantités utilisées pour l'analyser, car plusieurs des mots n'arrivent pas a avoir 100 ou 1000 résultats trouvés dans le systeme de recherche a analyser, donc l'analysis peut etre biaisés par le poor performance par rapport de ces terms.


#### Q5
> Comment on a déjà analysé, les principals métrics qui changent sont : num_rel_ret et les métrics de précision (map, gm_ap, R-prec, bpref). Cet effet est occasionné par les valeurs de score qui, chaque similarity choisi pour la comparaison, donne. Ainsi le calcul effectué pour qualifier le système  est affecté par ces valeurs et en plus, pour la quantité des résultats non pertinents (un autre effet de calcul du score). Un autre métric pour se rendre compte que l'augmentation de résultat ne change pas les précisions (au moins dans ce cas-la) sont les valeurs  (P5, P10, P15, P20, etc ) qui sont fixées pour les premiers résultats.

> On peut voir que le pire résultat (TF Log / IDF Smooth) a augmenté de 9 a 13 résultats (num_rel_ret) mais le score reste practiquement le même, c'est a cause de la position peu pertinent des résultats ajoutés (plus de 100eme place).
> Pour le meilleur résultat on trouve que BM25 récupere 2 résultats supplémentaires et le moyenne devient 0.3402 (avant 0.3391).