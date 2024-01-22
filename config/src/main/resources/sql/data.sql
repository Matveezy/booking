INSERT into users
    (name, login, pass, date_of_birth, role)
VALUES ('Ivan1233', 'ivan@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'USER'),
       ('Alex', 'alex@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '1999-02-06', 'USER'),
       ('Julia', 'julia@mail.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2003-01-09', 'USER'),
       ('Bob', 'bob123@yahoo.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2006-06-01',
        'USER'),
       ('Kate', 'kate@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2001-03-02', 'USER'),
       ('Jane', 'jane223@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2004-09-10',
        'USER'),
       ('Matvey', 'matveezy@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'ADMIN'),
       ('Anvar', 'anvar@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'ADMIN'),
       ('Alexandr', 'alexandr@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'OWNER'),
       ('Mihail', 'jackson@gmail.com', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'OWNER'),
       ('Valeriy', 'butorin.v.a@mail.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'ADMIN');

INSERT into hotel
    (name, hotelclass, city)
VALUES ('Domina St. Petersburg', 'THREE_STARS', 'Saint-Petersburg'),
       ('Taleon Imperial', 'FIVE_STARS', 'Saint-Petersburg'),
       ('Novotel', 'FOUR_STARS', 'Saint-Petersburg'),
       ('W St. Petersburg', 'ONE_STAR', 'Saint-Petersburg'),
       ('SO/St Petersburg', 'FIVE_STARS', 'Saint-Petersburg'),
       ('Solo Sokos Hotel Palace Bridge', 'FOUR_STARS', 'Saint-Petersburg'),
       ('Kempinski Hotel Moika 22', 'THREE_STARS', 'Amsterdam');

INSERT into guestinfo
    (name, surname, birth_date, passport)
VALUES ('Ivan', 'Ivanov', '2000-01-01', '12345666'),
       ('Petr', 'Petrov', '1972-04-02', '12445666'),
       ('Svetlana', 'Svetikova', '1999-12-31', '12345666'),
       ('Vladimir', 'Vasiliev', '1998-08-06', '12345666');

INSERT INTO room
    (hotelid, roomclass, roomnumber, price)
VALUES ((SELECT id from hotel where name = 'Novotel'), 'TRIPLE', 1339, 4000),
       ((SELECT id from hotel where name = 'Novotel'), 'SINGLE', 1338, 2500),
       ((SELECT id from hotel where name = 'Novotel'), 'DOUBLE', 1337, 3200),
       ((SELECT id from hotel where name = 'Novotel'), 'QUAD', 1342, 5000),
       ((SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'TRIPLE', 12, 6000),
       ((SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'SINGLE', 456, 3600),
       ((SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'DOUBLE', 26, 5000),
       ((SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'QUAD', 998, 8500),
       ((SELECT id from hotel where name = 'Domina St. Petersburg'), 'TRIPLE', 1, 2800),
       ((SELECT id from hotel where name = 'Domina St. Petersburg'), 'SINGLE', 2, 1900),
       ((SELECT id from hotel where name = 'Domina St. Petersburg'), 'DOUBLE', 3, 2400),
       ((SELECT id from hotel where name = 'Domina St. Petersburg'), 'QUAD', 4, 3200);

insert into hotelowner
    (hotelid, userid)
values ((SELECT id from hotel where name = 'Novotel'), (SELECT id from users where login = 'alexandr@gmail.com')),
       ((SELECT id from hotel where name = 'Domina St. Petersburg'),
        (SELECT id from users where login = 'jackson@gmail.com'));

INSERT into wallet
    (userid, balance)
VALUES ((SELECT id from users where login = 'alex@yandex.ru'), 200),
       ((SELECT id from users where login = 'julia@mail.ru'), 300),
       ((SELECT id from users where login = 'bob123@yahoo.com'), 400),
       ((SELECT id from users where login = 'kate@gmail.com'), 500),
       ((SELECT id from users where login = 'jane223@gmail.com'), 350),
       ((SELECT id from users where login = 'ivan@gmail.com'), 10000),
       ((SELECT id from users where login = 'alexandr@gmail.com'), 2000);


INSERT INTO orders
    (date_in, date_out, created_at, room_id, hotel_id, user_id)
VALUES ('2023-10-10', '2023-10-12', now()::timestamp, (select id from room where roomnumber = 1339), 3,
        (SELECT id from users where login = 'alex@yandex.ru')),
       ('2023-12-29', '2023-12-31', now()::timestamp, (select id from room where roomnumber = 1338), 3,
        (SELECT id from users where login = 'julia@mail.ru')),
       ('2024-01-01', '2024-01-03', now()::timestamp, (select id from room where roomnumber = 1337), 3,
        (SELECT id from users where login = 'bob123@yahoo.com')),
       ('2023-11-10', '2023-11-12', now()::timestamp, (select id from room where roomnumber = 4), 3,
        (SELECT id from users where login = 'kate@gmail.com'));




