--liquibase formatted sql

--changeset matthew:1
ALTER TABLE hotelowner
    ADD constraint fk_hot_on_user
        FOREIGN KEY (userid) REFERENCES users (id);

--changeset matthew:2
ALTER TABLE wallet
    ADD constraint fk_wallet_on_user
        FOREIGN KEY (userid) REFERENCES users (id);



