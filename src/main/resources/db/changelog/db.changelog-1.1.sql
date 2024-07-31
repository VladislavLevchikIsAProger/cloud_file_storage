--liquibase formatted sql

--changeset vlevchik:1
CREATE INDEX idx_username ON Users (Username);