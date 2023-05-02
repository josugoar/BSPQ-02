/* DELETE 'learnifyDB' database*/
DROP SCHEMA IF EXISTS learnifyDB;
/* DELETE USER 'spq' AT LOCAL SERVER*/
DROP USER IF EXISTS 'spq'@'localhost';

/* CREATE 'learnifyDB' DATABASE */
CREATE SCHEMA learnifyDB;
/* CREATE THE USER 'spq' AT LOCAL SERVER WITH PASSWORD 'spq' */
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';

GRANT ALL ON learnifyDB.* TO 'spq'@'localhost';
