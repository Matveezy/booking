INSERT into users
    (name, login, pass, date_of_birth, role)
VALUES ('Admin', 'admin@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2002-01-01',
        'ADMIN'),
       ('User', 'user@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '1999-02-06', 'USER'),
       ('Owner', 'owner@yandex.ru', '$2a$10$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze', '2003-01-09',
        'OWNER');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) from users));
