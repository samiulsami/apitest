# Bookstore API server

A simple bookstore API server written as an excersice for learning Java.

## Installation

Download Java and Maven, and set up their path variables

## Running the server

`git clone https://github.com/samiulsami/apitest.git`

`cd apitest`

`mvn package`

`java -jar target/apitest-1.0-SNAPSHOT-jar-with-dependencies.jar`


## API Endpoints

|method|url|body|actions|
|---|---|---|---|
|GET|`https://localhost:8000/bookstore/login`|--header 'Authorization: Basic c2FtaToxMjM0'|returns a JWT token $TOKEN|
|GET|`https://localhost:8000/bookstore/books`|--header 'Authorization: $TOKEN'|returns all books|
|GET|`https://localhost:8000/bookstore/books/{id}`|--header 'Authorization: $TOKEN'|returns book with given id|
|POST|`https://localhost:8000/bookstore/books`|--header 'title: $TITLE' ...| adds a book|
|PUT|`http://localhost:8000/bookstore/books/{id}`|--header 'title: $TITLE' ...|updates book with given id|
|DELETE|`http://localhost:8000/bookstore/books/{id}`|--header 'Authorization: $TOKEN'|deletes book with given id|

---

## Some cURL commands

TODO

## References

TODO


## Docker

TODO


## Make File
TODO