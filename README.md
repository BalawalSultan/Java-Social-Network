# Java Based Social Network

This is a Java based terminal application that simulates a social network on your terminal.
This application was developed using Java for the logic

## Application Architecture
To read the architecture open the [report.md](https://github.com/BalawalSultan/ProgrammingProject/blob/master/REPORT.md) file.

## Installation

Before setting up the java application you must create a PostgreSql database called
twitaroo using the Twitaroo.sql. Once the database has been created you can set the
database user and password in the App.java file.

Finally use [Maven](https://maven.apache.org/) to prepare the application.

```bash
mvn clean install compile package
```

### How to run the application

Once the input file is ready use the following command to run the application.

```bash
mvn clean package exec:exec
```