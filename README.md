
# Project Title

A brief description of what this project does and who it's for

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
|POST|`https://localhost:8000/bookstore/books`|  given below| adds a book|
|PUT|`http://localhost:8000/bookstore/books/{id}`| given below|updates book with given id|
|DELETE|`http://localhost:8000/bookstore/books/{id}`|--header 'Authorization: $TOKEN'|deletes book with given id|

---

## Some cURL commands
#### Login and receive a JWT $TOKEN (username: sami, password: 1234)
```
curl --location 'http://localhost:8000/bookstore/login' \
--header 'Authorization: Basic c2FtaToxMjM0'
```
#### Show all books
```
curl --location 'http://localhost:8000/bookstore/books' \
--header 'Authorization: $TOKEN'
```
#### Show book with given {id}
```
curl --location 'http://localhost:8000/bookstore/books/1' \
--header 'Authorization: $TOKEN'
```
#### Add book
```
curl --location --request POST 'http://localhost:8000/bookstore/books' \
--header 'title: d12312' \
--header 'authorName: askdj' \
--header 'authorID: 123' \
--header 'pages: 111' \
--header 'Authorization: $TOKEN'
```
#### Update book with given {id}
```
curl --location --request PUT 'http://localhost:8000/bookstore/books/7' \
--header 'title: updated book' \
--header 'authorName: George Orwell' \
--header 'pages: 140' \
--header 'authorID: 2' \
--header 'Authorization: $TOKEN'
```
#### Delete book with given {id}
```    
curl --location --request DELETE 'http://localhost:8000/bookstore/books/7' \
--header 'Authorization: $TOKEN'
```
----

## References

- https://metamug.com/article/security/jwt-java-tutorial-create-verify.html
- https://github.com/metamug/java-jwt/blob/master/src/test/java/com/metamug/jwt/JWebTokenTest.java
- https://medium.com/consulner/framework-less-rest-api-in-java-dd22d4d642fa
- https://github.com/heheh13/api-server
- https://github.com/MobarakHsn/api-server/tree/master
- https://github.com/AbdullahAlShaad/bookstore-api-server


## Docker

TODO


## Make File
TODO