--liquibase formatted sql

--changeset matthew:1
CREATE TABLE IF NOT EXISTS orders
(
    id         BIGSERIAL PRIMARY KEY,
    hotel_id   BIGSERIAL REFERENCES hotel (id),
    user_id    BIGSERIAL REFERENCES users (id),
    date_in    TIMESTAMP      NOT NULL,
    date_out   TIMESTAMP      NOT NULL,
    created_at TIMESTAMP NOT NULL,
    room_id    BIGINT REFERENCES room (id)
    );


