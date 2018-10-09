# EMIND1G1 : Fondements de la recherche d'information

## Jesús Lovón Melgarejo

### TP1

#### 3 Analyses et exploration du code pour l’indexation et la recherche
* Q3.2

<table>
  <tbody>
    <tr>
      <th></th>
      <th align="center">Classes</th>
      <th align="center">Méthodes</th>
    </tr>
    <tr>
      <td><b>Lecture du CSV</b></td>
      <td align="left">
			<ul>
				<li>CSVFormat</li>
				<li>CSVParser</li>
				<li>CSVRecord</li>
			</ul>
		</td>
      <td align="left">
			<ul>
				<li>parse(Reader in)</li>
				<li>get(int i)</li>
			</ul>
		</td>
    </tr>
    <tr>
      <td><b>Indexation</b></td>
      <td align="left">
			<ul>
				<li>IndexWriterConfig</li>
				<li>IndexWriter</li>
			</ul>
		</td>
      <td align="left">
			<ul>
				<li>setOpenMode()</li>
				<li>addDocument()</li>
			</ul>
		</td>
    </tr>
    <tr>
      <td><b>Exécution de la requête</b></td>
      <td align="left">
			<ul>
				<li>Query</li>
				<li>IndexSearcher</li>
				<li>TopScoreDocCollector</li>
			</ul>
		</td>
      <td align="left">
			<ul>
				<li>search(Query, TopScoreDocCollector)</li>
				<li>topDocs()</li>
			</ul>
		</td>
    </tr>
  </tbody>
</table>


* Q3.3: Cherchez sur la documentation de lucene les différents Analyzers et identifiez leurs différences.
Pourquoi on a besoin de plusieurs Analyzers ?


> Il y a plusieurs Analyzers pour Lucene Core (par exemple: StandardAnalyser, StopAnalyser, SimpleAnalyser, KeywordAnalyzer, CustomAnalyzer) et autres hors du core (par exemple: Analyzers-ICU, Analyzers-kuromoji, Analzers-opennlp, etc).

> En general, un Analyzer de Lucene utilise une combination de tokenizer et filtres avec differentes criterias qui sont justifiées selon l’utilisation ou application. Ainsi, si nous avons besoin de identifier  une langue en particulier (p.e Anglais) nous pouvons utiliser le EnglishAnalyzer, ou WhiteSpaceAnalyzer pour identifier tous les mots separé pour une space blanche dans un document.

* Q3.4: Qu'est-ce que la classe TextField et à quoi elle serve ? Pouvez vous utiliser une autre classe ?
Laquelle ?

> Les classes Fields sont les sections des Documents. Chaque Classe est d’un type particulier. Pour la classe TextField, le text est  transformé en tokens et indexé. Une classe pareille à TextField est StringField mais on ne peut pas l’utiliser parce qu’elle n’a pas de tokens, alors il serait impossible de réaliser une requête après.


#### 4 Reconfiguration de l’indexation
Voir commits


#### 5  Questions ouvertes

* Q5.1: Renseignez vous sur Terrier ou/et Solr ou/et ElasticSearch et essayez de faire le même index.
Quelles différences identifiez-vous ?

> Terrier, Solr et ElasticSearch sont des logiciels ou systemes qui permettent de faire la recherche et indexation des documents sans besoin de connaître les librairies ou code des tiers. Alors, ils utilisent une metalangage pour configurer les paramètres selon les requêtes ou actions nécessaires.

> Ainsi, par exemple pour le cas de ElasticSearch, pour ajouter les fichiers à l'indexation il faut faire juste: curl -XPUT ‘data’. Par contre, en Lucene il faut bien connaître le structure des classes ou objets fondamentales.


* Q5.2: Pour Lucene, où sont les « fichiers d’indexation » mentionnés dans la diapositive 34 du cours ?
Pourquoi sont-ils nécessaires ?

> Dans le code on a spécifier le fichier ‘indexRI’ pour garder les indexes (qui sont des fichiers binaires). Après Lucene va les lire avec un IndexReader pour calculer le score de pertinence et montrer les résultat aux prochaines requêtes.
