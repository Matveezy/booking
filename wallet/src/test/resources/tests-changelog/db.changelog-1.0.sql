--liquibase formatted sql

--changeset matthew:1
CREATE TABLE wallet
(
    userid  BIGINT,
    balance BIGINT NOT NULL,
    id      BIGSERIAL PRIMARY KEY
);

INSERT INTO wallet
    (userid, balance)
VALUES (1, 1000),
       (2, 2500),
       (3, 4250),
       (4, 0);