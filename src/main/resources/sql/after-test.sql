SELECT setval(pg_get_serial_sequence('room', 'id'), 1) FROM room;
SELECT setval(pg_get_serial_sequence('hotel', 'id'), 1) FROM hotel;
SELECT setval(pg_get_serial_sequence('users', 'id'), 1) FROM users;
