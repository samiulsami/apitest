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
|GET|`https://localhost:8000/bookstore/login`||--header 'Authorization: Basic c2FtaToxMjM0'|returns a JWT token $TOKEN
|GET|`https://localhost:8000/bookstore/books`|--header 'Authorization: $TOKEN'|returns all books|
|GET|`https://localhost:8000/bookstore/books/{id}`|--header 'Authorization: $TOKEN'|returns book with given id|
|POST|`https://localhost:8000/bookstore/books`|--header 'title: $TITLE' ...| adds a book|
|PUT|`http://localhost:8000/bookstore/books/{id}`|--header 'title: $TITLE' ...|updates book with given id|
|DELETE|`http://localhost:8000/bookstore/books/{id}`|--header 'Authorization: $TOKEN'|deletes book with given id|

---

## basic curl commands

Get all users

    curl -X GET --user heheh13:12345 http://localhost:8080/api/users

Delete Specific user

    curl -X DELETE --user heheh13:12345 http://localhost:8080/api/users/1

Add a user

    curl -X POST -d '{"id":"1","name":"Mehedi Hasan","skills":{"language":["c++,go"],"tools":["git","linux"],"endorsed":0}}' --user heheh13:12345 http://localhost:8080/api/users

Update a user

    curl -X PUT -d '{"name":"heheh"}' --user heheh13:12345 http://localhost:8080/api/users/1

## References

TODO


## Docker

TODO


## make file
TODO