insert into users (name, login, pass, date0fbirth, role, id)
values
    ('users','user_login','user_pass','2020-10-05 14:01:10-08', 'ADMIN', 100),
    ('owner','owner_login','owner_pass','2020-10-05 14:01:10-08', 'ADMIN', 200);


insert into wallet (userid, balance, id)
values
    (100,1,100000),
    (200,2,9000000);

insert into hotel (name, hotelclass, city, id)
values
    ('hotel1','FIVE_STARS','London',10000),
    ('hotel2','ONE_STAR','Moscow',100),
    ('hotel3','THREE_STARS','Paris',200),
    ('hotel4','FOUR_STARS','Los-Angeles',300),
    ('hotel5','ONE_STAR','Stockholm',400),
    ('hotel6','ONE_STAR','Paris',500),
    ('hotel7','FOUR_STARS','London',600),
    ('hotel8','ONE_STAR','Moscow',700),
    ('hotel9','THREE_STARS','New-York',800),
    ('hotel10','FIVE_STARS','Amsterdam',900),
    ('hotel11','FIVE_STARS','Paris',1000),
    ('hotel12','ONE_STAR','Paris',1100),
    ('hotel13','THREE_STARS','Beijing',1200),
    ('hotel14','THREE_STARS','Chicago',1300),
    ('hotel15','FIVE_STARS','Paris',1400),
    ('hotel16','FOUR_STARS','London',1500),
    ('hotel17','ONE_STAR','Omsk',1600),
    ('hotel18','ONE_STAR','Paris',1700),
    ('hotel19','FIVE_STARS','Oslo',1800),
    ('hotel20','FIVE_STARS','Rio',1900),
    ('hotel21','ONE_STAR','Helsinki',2000);


insert into room (hotelid, roomclass, roomnumber, price, id)
values
    (10000,'SINGLE',1,1,100),
    (10000,'SINGLE',1,1,200),
    (10000,'SINGLE',1,1,300);

insert into hotelowner (hotelid, userid)
    values (10000, 200);