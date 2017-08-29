# phonebook
The phonebook is developed with AngularJS, Bootstrap, Dropwizard framework, JDBI and Mysql database

## Build
Git clone this repository

Run mvn package to get all packages

## Create database
Configure config.yml if you don't want to use phonebook database schema.

Run ddl.sql file to create database and tables. It inserts some fake data to give the feel of index page. we can delete this fake data.

## Run 
Either java -jar target/phonebook-0.0.1-SNAPSHOT.jar server config.yml

-or- in eclipse Run Application with Configuration as program arguments as server config.yml for dropwizard.

On your browser you will run http://localhost:8080/phonebook/index.html

## test
src/main/postman has exported json to test all REST APIs

