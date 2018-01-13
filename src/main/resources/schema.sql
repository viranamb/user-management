CREATE TABLE users
(
    id int(11) NOT NULL AUTO_INCREMENT,
    firstName  varchar(100) NOT NULL,
    lastName varchar(100) NOT NULL,
    userName  varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    emailAddress varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);