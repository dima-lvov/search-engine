#Search Engine App
Consists of 3 Maven modules:

* search-api - DTOs used by the client and server.
* client     - client to consume REST API. Executes hardcoded logic to call server.
* server     - SpringBoot document rest service exposes create, findByKey and search by set of tokens (words) API.


## Requirements

For building and running the application you need:

- [JDK 11](https://www.codejava.net/java-se/download-and-install-java-11-openjdk-and-oracle-jdk)
- [Maven 3](https://maven.apache.org)

## Running the application locally

### Build and test multi module maven project 

```shell
cd <folder-with-project>\search-engine-app
mvn clean install
```

### Running Search Engine server

There are several ways to run a Spring Boot Search Engine application on your local machine. One way is to execute the `main` method in the `com.example.search.server.SearchEngineApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:
```shell
cd <folder-with-project>\search-engine-app\server
mvn spring-boot:run
```
After this rest server is up and running on http://localhost:8080.

### Service REST API

Create the document by key:

```
POST: /documents

Content-Type:application/json

{
"documentKey": "someKey",
"content": "token1 token2"
}
```

Get the document by key:
```
GET: /documents/{documentKey}

```

Search keys of documents containing set of tokens:
```
GET: /documents?token=token1,token2

```

### To test it all at once please run the hardcoded client app:

One way is to execute the `main` method in the `com.example.search.client.HardcodedClientApplication` class from your IDE.

Alternatively you can use the [Exec Maven plugin](https://www.mojohaus.org/exec-maven-plugin/) like so:

```shell
cd <folder-with-project>\search-engine-app\client
mvn compile exec:java
```

### Information about spent time

~ 9 - 10 hours 

