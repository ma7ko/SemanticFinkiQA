# SemanticFinkiQA

This is web application that uses RDF triples as data source.
In order to start the application successfully you need to have jena and jena-fuseki on your machine.

In order to make your own data store you can use the jena tdbloader command where you give the location of your store and the file containing triples (example: in turtle format):

**tdbloader   --loc=path/where/the/store/is/created   path/to/file/containing/triples.ttl**

In order to start the fuseki server with the store you already have or have previously created use the following command:

**fuseki-server   --loc=path/where/the/store/is/created   /nameOfYourDataset**
