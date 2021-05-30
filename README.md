# Java Based Social Network

This is a Java based terminal application that simulates a social network on your terminal.

## Application Architecture
To read the architecture open the [report.md](https://github.com/BalawalSultan/ProgrammingProject/blob/master/REPORT.md) file.

## Installation

Before setting up the java application you must create a PostgreSql database called twitaroo using the Twitaroo.sql.
Finally use [Maven](https://maven.apache.org/) to prepare the application.

```bash
mvn clean install compile package
```

### How to run the application


```bash
mvn exec:exec
```