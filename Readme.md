# EMIND1G1 : Fondements de la recherche d'information

## Jesús Lovón Melgarejo

### TP1

* Q3.2

<table>
  <tbody>
    <tr>
      <th></th>
      <th align="center">Classes</th>
      <th align="right">Méthodes</th>
    </tr>
    <tr>
      <td><b>Lecture du CSV</b></td>
      <td align="center">
			<ul>
				<li>CSVFormat</li>
				<li>CSVParser</li>
				<li>CSVRecord</li>
			</ul>
		</td>
      <td align="center">
			<ul>
				<li>parse(Reader in)</li>
				<li>get(int i)</li>
			</ul>
		</td>
    </tr>
    <tr>
      <td><b>Indexation</b></td>
      <td align="center">
			<ul>
				<li>IndexWriterConfig</li>
				<li>IndexWriter</li>
			</ul>
		</td>
      <td align="center">
			<ul>
				<li>setOpenMode()</li>
				<li>addDocument()</li>
			</ul>
		</td>
    </tr>
    <tr>
      <td><b>Exécution de la requête</b></td>
      <td align="center">
			<ul>
				<li>Query</li>
				<li>IndexSearcher</li>
				<li>TopScoreDocCollector</li>
			</ul>
		</td>
      <td align="center">
			<ul>
				<li>search(Query, TopScoreDocCollector)</li>
				<li>topDocs()</li>
			</ul>
		</td>
    </tr>
  </tbody>
</table>


* Q3.3
Il y a plusieurs Analyzers pour Lucene Core (par exemple: StandardAnalyser, StopAnalyser, SimpleAnalyser, KeywordAnalyzer, CustomAnalyzer) et autres hors du core (par exemple: Analyzers-ICU, Analyzers-kuromoji, Analzers-opennlp, etc).

En general, un Analyzer de Lucene utilise une combination de tokenizer et filtres avec differentes criterias qui sont justifiées selon l’utilisation ou application. Ainsi, si nous avons besoin de identifier  une langue en particulier (p.e Anglais) nous pouvons utiliser le EnglishAnalyzer, ou WhiteSpaceAnalyzer pour identifier tous les mots separé pour une space blanche dans un document.

* Q3.4

Les classes Fields sont les sections des Documents. Chaque Classe est d’un type particulier. Pour la classe TextField, le text est  transformé en tokens et indexé. Une classe pareille à TextField est StringField mais on ne peut pas l’utiliser parce qu’elle n’a pas de tokens, alors il serait impossible de réaliser une requête après.

* Q5.1


