--liquibase formatted sql

--changeset matthew:1
ALTER TABLE guestinfo
drop
column dateofbirth,
    add column birth_date timestamp not null;


