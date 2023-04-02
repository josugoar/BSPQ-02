/* DELETE 'universityDB' database*/
DROP SCHEMA IF EXISTS universityDB;
/* DELETE USER 'spq' AT LOCAL SERVER*/
DROP USER IF EXISTS 'spq'@'localhost';

/* CREATE 'universityDB' DATABASE */
CREATE SCHEMA universityDB;
/* CREATE THE USER 'spq' AT LOCAL SERVER WITH PASSWORD 'spq' */
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';

GRANT ALL ON universityDB.* TO 'spq'@'localhost';
