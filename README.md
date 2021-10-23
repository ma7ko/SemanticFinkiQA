# SemanticFinkiQA

This is web application that uses RDF triples as data source.
In order to start the application successfully you need to have jena and jena-fuseki installed on your machine, added in environment variables.
You also should have Java and Apcache Maven (binary file added in environment variables) installed on your machine. 

In order to make your own data store you can use the jena tdbloader command where you give the location of your store and the file containing triples (example: in turtle format):

**tdbloader   --loc=path/where/the/store/is/created   path/to/file/containing/triples.ttl**

*You can skip the step above and use the created store in the folder fuseki-help called updatedTripleStore)*

In order to start the fuseki server with the store you already have or have previously created use the following command:

**fuseki-server   --loc=path/where/the/store/is/created   /nameOfYourDataset**

*The name for the dataset is hardcoded in the spring boot application and it is **new***
Use the following command to start the fuseki server with the updatedTripleStore from the fuseki-help folder:

**fuseki-server --loc=path/to/updatedTripleStore /new**

To start the Spring Boot application locate to the folder of this application and execute the following line in commant prompt:

**mvn spring-boot:run**

To start the React front-end part of this application run

**npm start**

*Summary: You should have three command prompt windows 
 one running the fuseki server, 
 the second one running the spring boot application and 
 the third running the React app*
