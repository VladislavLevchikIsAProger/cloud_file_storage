--liquibase formatted sql

--changeset vlevchik:2
CREATE TABLE folders
(
    Id      BIGSERIAL PRIMARY KEY,
    Name    VARCHAR UNIQUE NOT NULL,
    Color   VARCHAR(7)     NOT NULL,
    User_id INT            NOT NULL,
    FOREIGN KEY (User_Id) REFERENCES Users (ID)
);