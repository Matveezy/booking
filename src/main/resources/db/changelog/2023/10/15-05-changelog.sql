--liquibase formatted sql

--changeset matthew:1
ALTER TABLE "user"
RENAME to users;