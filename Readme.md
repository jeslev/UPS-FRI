
# EMIND1G1 : Fondements de la recherche d'information

## Jesús Lovón Melgarejo

### TP2

#### 1 Inspectez le code et écrivez la formule utilisée pour la classe BM25Similarity.
> On connait la composition du BM25 Similarity par la formule suivante:
> 
![BM2Similarity](https://wikimedia.org/api/rest_v1/media/math/render/svg/43e5c609557364f7836b6b2f4cd8ea41deb86a96)

> On peut la trouver dans le code de Lucene divisé en petites parties, qui ensemble, donnent la formule totale. Dans le fonction *computerWeight* on peut voir l'implementation du 2eme factor de dénominateur de la formule comme  ***k1 * ((1 - b) + b * LENGTH_TABLE[i] / avgdl);***

```java
public final SimWeight computeWeight(float boost, CollectionStatistics collectionStats, TermStatistics... termStats) {
    Explanation idf = termStats.length == 1 ? idfExplain(collectionStats, termStats[0]) : idfExplain(collectionStats, termStats);
    float avgdl = avgFieldLength(collectionStats);

    float[] oldCache = new float[256];
    float[] cache = new float[256];
    for (int i = 0; i < cache.length; i++) {
      oldCache[i] = k1 * ((1 - b) + b * OLD_LENGTH_TABLE[i] / avgdl);
      cache[i] = k1 * ((1 - b) + b * LENGTH_TABLE[i] / avgdl);
    }
    return new BM25Stats(collectionStats.field(), boost, idf, avgdl, oldCache, cache);
  }
```

> Ensuite, le constructeur de la classe _BM25DocScorer_ récupère le valeur du numérateur comme: idf*(k+1) qui est gardé dans le variable *weigthValue*
```java
BM25DocScorer(BM25Stats stats, int indexCreatedVersionMajor, NumericDocValues norms) throws IOException {
      this.stats = stats;
      this.weightValue = stats.weight * (k1 + 1);
      this.norms = norms;
      if (indexCreatedVersionMajor >= 7) {
        lengthCache = LENGTH_TABLE;
        cache = stats.cache;
      } else {
        lengthCache = OLD_LENGTH_TABLE;
        cache = stats.oldCache;
      }
    }
```
> Finalement, on utilise tous ces termes dans le fonction score pour calculer le score BM25 final, avec l’aide du valeur de freq et la variable norm (qui garde le valeur initial de computeWeigth).
```java
public float score(int doc, float freq) throws IOException {
      // if there are no norms, we act as if b=0
      float norm;
      if (norms == null) {
        norm = k1;
      } else {
        if (norms.advanceExact(doc)) {
          norm = cache[((byte) norms.longValue()) & 0xFF];
        } else {
          norm = cache[0];
        }
      }
      return weightValue * freq / (freq + norm);
    }
```

#### 2 Implementation BM11 et BM25.
- BM11 (b = 1) [classe BM11Similarity](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/BM11Similarity.java)
- BM15 (b = 0) [classe BM15Similarity](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/BM15Similarity.java)

#### 3 Variations de TF.
- Total  [classe TFTotal](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/TFTotal.java)
- Max  [classe TFMax](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/TFMax.java)
 - Sum  [classe TFSum](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/TFSum.java)
 - Log  [classe TFLog](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/TFLog.java)
 - Frac  [classe TFFrac](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/TFFrac.java)
 - BM25  [classe TFBM25](https://github.com/jeslev/UPS-FRI/blob/TP2/src/tp1/TFBM25.java)


#### 4 Variations de IDF.


#### 5 Utilisez plusieurs des méthodes des points précédents avec la requête « the white house ». Identifiez les différences parmi toutes les méthodes que vous avez implémentée. Avec les premiers 5 résultats pouvez vous dire quelle méthode vous semble la plus pertinente ? Pourquoi ?.

#### 6 Faites une combinaison en utilisant les deux/trois méthodes plus performantes que vous avez identifié dans le point 5. Les résultats obtenus sont-ils meilleurs ? Justifiez vous réponses.

#### 7 Effectuez une requête par titre et puis par contenue des pages Wikipédia. Trouvez vous de différences ? Les méthodes sont-ils plus ou moins pertinents selon le champ utilisé ? Pourquoi ?
