INSERT INTO mpa (name) VALUES('G');
INSERT INTO mpa (name) VALUES('PG');

INSERT INTO genre (name) VALUES('Комедия');
INSERT INTO genre (name) VALUES('Драма');

INSERT INTO film (name,description,releaseDate,duration,mpa_id)
    VALUES('Film1'
    ,'Description1'
    ,CAST('1988-07-12' as date)
    ,133
    ,2
    );


INSERT INTO film (name,description,releaseDate,duration,mpa_id)
    VALUES('Film2'
    ,'Description2'
    ,CAST('2006-04-13' as date)
    ,110
    ,1
    );


INSERT INTO users (email,login,name,birthday)
    VALUES ('test@comp.ru','testuser','Ivanov Ivan Ivanovich', '1982-11-21');
INSERT INTO users (email,login,name,birthday)
    VALUES ('test2@comp.ru','testuser2','Petrov Petr Petrovich', '1990-02-16');

--MERGE INTO film_like (film_id,user_id) KEY (film_id,user_id)  VALUES (1,1);
--MERGE INTO film_like (film_id,user_id) KEY (film_id,user_id)  VALUES (1,2);