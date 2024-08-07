--liquibase formatted sql

--changeset vlevchik:3
CREATE TABLE favorites
(
    Id       BIGSERIAL PRIMARY KEY,
    Filename VARCHAR NOT NULL,
    Filepath VARCHAR NOT NULL,
    Size VARCHAR NOT NULL,
    User_id  BIGINT NOT NULL,
    FOREIGN KEY (User_id) REFERENCES Users (Id),
    CONSTRAINT unique_filename_filepath UNIQUE (Filename, Filepath)
);