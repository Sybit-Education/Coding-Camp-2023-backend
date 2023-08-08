# Sygotchi - Backend

## Introduction

This is the backend of the Sygotchi project. It is written in Java using the Spring Boot framework. The project is built using Maven.

## Architecture

The project is split into three layers: 
    The controller layer, 
    the service layer 
    and the repository layer. 
    The controller layer is responsible for handling the requests and responses. The service layer is responsible for the business logic. The repository layer is responsible for the database access.


## API Documentation

The API documentation is available at [http://localhost:8080/docs](http://localhost:8080/docs).

## Authentication and Authorization

The authentication is done using JWT. The JWT is sent in the Authorization header of the request. The JWT is signed using a secret. The secret is stored in the environment variable JWT_SECRET. The JWT contains the user id of the user. The user id is used to identify the user. The JWT is valid for 30 days. The JWT is refreshed every time the user sends a request
too the backend. The JWT is refreshed by sending a new JWT in the Authorization header of the response. The JWT is refreshed by the JwtFilter.


## Database

The database is a In memory database. The database is automatically migrated using Flyway. The migrations are located in the src/main/resources/db/migration folder.

