USE learnifyDB;

/* CREATE AN ACCOUNTs*/
INSERT IGNORE INTO USER(LOGIN,NAME,PASSWORD, ROLE, SURNAME) VALUES ("admin", "admin", "admin", "ADMIN", "admin" );
INSERT IGNORE INTO USER(LOGIN,NAME,PASSWORD, ROLE, SURNAME) VALUES ("professor", "professor", "professor", "PROFESSOR", "professor" );
INSERT IGNORE INTO USER(LOGIN,NAME,PASSWORD, ROLE, SURNAME) VALUES ("student", "student", "student", "STUDENT", "student" );
