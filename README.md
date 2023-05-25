Learnify
========

Run the following command to compile all classes and launch the unit tests:

      mvn test

Or without tests;

      mvn clean compile

Make sure that the database was correctly configured. Use the contents of the file *create-learnify.sql* to create the database and grant privileges. For example,

      mysql -p --user root < sql/create-learnify.sql

Alternatively, on Windows, enter MySQL shell with root with the following command:

      mysql -p --user root
      source sql/create-learnify.sql

The class enhancement required by DataNucleus must be manually executed after the unit testing is performed.
This is required to avoid cluttering the JaCoCo report with all the methods generated automatically by DataNucleus.

Therefore, execute the following command to enhance the database classes

      mvn datanucleus:enhance

Run the following command to create database schema for this sample.

      mvn datanucleus:schema-create

Integration tests can be launched using the following command. An embedded Grizzly HTTP server will be launched to perform real calls
to the REST API and to the MySQL database.

      mvn verify -Pintegration-tests

Performance tests can be launched using the following command.

      mvn verify -Pperformance-tests

Run the following command to create the data.

      mysql -p --user root < sql/create-data.sql

On Windows:

      mysql -p --user root
      source sql/create-data.sql  

To launch the server run the command

      mvn jetty:run

Now, the client sample code can be executed in a new command window with
      
      mvn exec:java -Pclient

To generate documentation write: 

      mvn doxygen:report

[Doxygen documentation](https://gaizka-basterra.github.io/Learnify/)
