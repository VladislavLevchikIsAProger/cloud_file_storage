--liquibase formatted sql

--changeset vlevchik:1
CREATE TABLE Users
(
    Id       BIGSERIAL PRIMARY KEY,
    Username VARCHAR NOT NULL UNIQUE,
    Password VARCHAR NOT NULL UNIQUE
);