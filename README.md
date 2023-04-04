Learnify
============================

This example relies on the DataNucleus Maven plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *pom.xml* file. In addition, the project contains the server and client example codes.

Run the following command to build everything and enhance the DB classes:

      mvn clean compile

Make sure that the database was correctly configured. Use the contents of the file *create-learnify.sql* to create the database and grant privileges. For example,

      mysql -p --user root < sql/create-learnify.sql

Alternatively, on Windows, enter MySQL shell with Root with the following command

      mysql -u root -p
      source sql/create-learnify.sql

Run the following command to create database schema for this sample.

      mvn datanucleus:schema-create

Run the following command to create an admin account.

      mysql -p --user root < sql/create-admin.sql

On Windows:

      mysql -u root -p
      source sql/create-admin.sql      

To launch the server run the command

      mvn jetty:run

Now, the client sample code can be executed in a new command window with

      mvn exec:java -Pclient
