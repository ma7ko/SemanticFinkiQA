# SemanticFinkiQA

#### Application overview
It is an application where users can ask questions and other users can answer them. Users have option for adding tags to the questions. 
#### Technical overview
This is web application that uses RDF triples as data source. 
The Front-end to Back-end communication happens with JSON-LD (JSON for linked data).

## Installation Requirements

In order to start the application successfully you need to have jena and jena-fuseki installed on your machine, added in environment variables.
You also should have Java and Apcache Maven (binary file added in environment variables) installed on your machine. 


### 1. Fuseki Server setup

####  Creating own Triple store
In order to make your own data store you can use the jena tdbloader command where you give the location of your store and the file containing triples (example: in turtle format):

    tdbloader   --loc=path/where/the/store/is/created   path/to/file/containing/triples.ttl

*You can skip the step above and use the created store in the folder fuseki-help called updatedTripleStore)*

In order to start the fuseki server with the store you already have or have previously created use the following command:

    fuseki-server   --loc=path/where/the/store/is/created   /nameOfYourDataset

####  Triple store from fuseki-help folder
*The name for the dataset is hardcoded in the spring boot application and it is **new***.
Use the following command to start the fuseki server with the updatedTripleStore from the fuseki-help folder:

    fuseki-server --loc=path/to/updatedTripleStore /new

### 2. Running Spring Boot Application

To start the Spring Boot application locate to the folder of this application and execute the following line in commant prompt:

    mvn spring-boot:run

### 3. Running React App

To start the React front-end part of this application run

    npm start


*Summary: You should have three command prompt windows 
 one running the fuseki server, 
 the second one running the spring boot application and 
 the third running the React app*
