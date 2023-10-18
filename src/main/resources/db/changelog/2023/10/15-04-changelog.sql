--liquibase formatted sql

--changeset matthew:1
ALTER TABLE "user"
ADD COLUMN role VARCHAR(16) DEFAULT 'USER';