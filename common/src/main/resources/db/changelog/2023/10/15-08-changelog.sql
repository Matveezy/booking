--liquibase formatted sql

--changeset matthew:1
ALTER TABLE wallet
drop
column id,
    add column id bigserial primary key;

--changeset matthew:2
ALTER TABLE room
drop
column id,
    add column id bigserial primary key;

--changeset matthew:3
ALTER TABLE hotel
    drop column id cascade ,
    add column id bigserial primary key;

ALTER TABLE room
    ADD constraint fk_room_on_hotelid
        FOREIGN KEY (hotelid) REFERENCES hotel (id);

ALTER TABLE hotelowner
    ADD constraint fk_hot_on_hotel
        FOREIGN KEY (hotelid) REFERENCES hotel (id);

--changeset matthew:4
ALTER TABLE guestinfo
drop column id,
    add column id bigserial primary key;


