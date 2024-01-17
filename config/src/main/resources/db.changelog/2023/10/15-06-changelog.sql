--liquibase formatted sql

--changeset matthew:1
ALTER TABLE users
    drop column id cascade ,
    add column id bigserial primary key;