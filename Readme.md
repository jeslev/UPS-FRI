
# EMIND1G1 : Fondements de la recherche d'information

## Jesús Lovón Melgarejo

### TP3

#### Q1 
> On trouve les résultats dans les positions suivantes:
12. Mario Gómez
25. Thomas Müller
> La page *FC Bayern Munich* n'est pas trouvé dans les premières 50 résultats.

#### Q2
> Selon les résultats ci-dessous on trouve la page *Mario Gomez* entre la position 12-13 pour BM25 et BM11 Similarity mais on ne trouve pas la page *FC Batern Munich*. Dans le cas contraire, avec les scores TFIDF et TFIDF (Log et BIR Smooth) on trouve la page du FC mais pas *Thomas Muller*. Finalement avec TFIDF (TF Total et BIR Smooth) on ne trouve aucune résultat pertinent.
- *BM25*:
12. Mario Gómez
25. Thomas Müller
- *BM11*:
13. Mario Gómez
30. Thomas Müller  
- *TFIDF*:
21. Mario Gómez
34. F.C. Bayern Munich
- *TF Log IDF BIR Smooth*:
25. Mario Gómez
40. F.C. Bayern Munich
- *TF Total IDF BIR Smooth*:

