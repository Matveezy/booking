--liquibase formatted sql

--changeset matthew:1
ALTER TABLE users
    RENAME COLUMN date0fbirth TO date_of_birth;