INSERT into hotel
    (id, name, hotelclass, city)
VALUES (1, 'Domina St. Petersburg', 'THREE_STARS', 'Saint-Petersburg'),
       (2, 'Taleon Imperial', 'FIVE_STARS', 'Saint-Petersburg'),
       (3, 'Novotel', 'FOUR_STARS', 'Saint-Petersburg'),
       (4, 'W St. Petersburg', 'ONE_STAR', 'Saint-Petersburg'),
       (5, 'SO/St Petersburg', 'FIVE_STARS', 'Saint-Petersburg'),
       (6, 'Solo Sokos Hotel Palace Bridge', 'FOUR_STARS', 'Saint-Petersburg'),
       (7, 'Kempinski Hotel Moika 22', 'THREE_STARS', 'Amsterdam');
SELECT SETVAL('hotel_id_seq', (SELECT MAX(id) from hotel));

INSERT INTO room
    (id, hotelid, roomclass, roomnumber, price)
VALUES (1, (SELECT id from hotel where name = 'Novotel'), 'TRIPLE', 1339, 4000),
       (2, (SELECT id from hotel where name = 'Novotel'), 'SINGLE', 1338, 2500),
       (3, (SELECT id from hotel where name = 'Novotel'), 'DOUBLE', 1337, 3200),
       (4, (SELECT id from hotel where name = 'Novotel'), 'QUAD', 1342, 5000),
       (5, (SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'TRIPLE', 12, 6000),
       (6, (SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'SINGLE', 456, 3600),
       (7, (SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'DOUBLE', 26, 5000),
       (8, (SELECT id from hotel where name = 'Solo Sokos Hotel Palace Bridge'), 'QUAD', 998, 8500),
       (9, (SELECT id from hotel where name = 'Domina St. Petersburg'), 'TRIPLE', 1, 2800),
       (10, (SELECT id from hotel where name = 'Domina St. Petersburg'), 'SINGLE', 2, 1900),
       (11, (SELECT id from hotel where name = 'Domina St. Petersburg'), 'DOUBLE', 3, 2400),
       (12, (SELECT id from hotel where name = 'Domina St. Petersburg'), 'QUAD', 4, 3200);
SELECT SETVAL('room_id_seq', (SELECT MAX(id) from room));

insert into hotelowner
    (hotelid, userid)
values (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7);

INSERT INTO orders
    (id, date_in, date_out, created_at, room_id, hotel_id, user_id)
VALUES (1, '2023-10-10', '2023-10-12', now()::timestamp, (select id from room where roomnumber = 1339), 3,
        1),
       (2, '2023-12-29', '2023-12-31', now()::timestamp, (select id from room where roomnumber = 1338), 3,
        2),
       (3, '2024-01-01', '2024-01-03', now()::timestamp, (select id from room where roomnumber = 1337), 3,
        3),
       (4, '2023-11-10', '2023-11-12', now()::timestamp, (select id from room where roomnumber = 4), 3,
        4);
SELECT SETVAL('orders_id_seq', (SELECT MAX(id) from orders));

INSERT into users
(id, name, login, pass, date_of_birth, role)
VALUES (1, 'Admin', 'admin@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'ADMIN'),
       (2, 'User', 'user@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '1999-02-06',
        'USER'),
       (3, 'Owner', 'owner@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2003-01-09',
        'OWNER');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) from users));



