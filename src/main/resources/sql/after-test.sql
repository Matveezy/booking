SELECT setval(pg_get_serial_sequence('room', 'id'), 1, FALSE);
SELECT setval(pg_get_serial_sequence('hotel', 'id'), 1, FALSE);
SELECT setval(pg_get_serial_sequence('users', 'id'), 1, FALSE);
