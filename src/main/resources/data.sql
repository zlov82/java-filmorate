select 1;

MERGE INTO mpa (name) KEY(name) VALUES('G');
MERGE INTO mpa (name) KEY(name) VALUES('PG');
MERGE INTO mpa (name) KEY(name) VALUES('PG-13');
MERGE INTO mpa (name) KEY(name) VALUES('R');
MERGE INTO mpa (name) KEY(name) VALUES('NC-17');

MERGE INTO genre (name) KEY(name) VALUES('Комедия');
MERGE INTO genre (name) KEY(name) VALUES('Драма');
MERGE INTO genre (name) KEY(name) VALUES('Мультфильм');
MERGE INTO genre (name) KEY(name) VALUES('Триллер');
MERGE INTO genre (name) KEY(name) VALUES('Документальный');
MERGE INTO genre (name) KEY(name) VALUES('Боевик');

/*

MERGE INTO film (name,description,releaseDate,duration,mpa_id) KEY(name,description,releaseDate,duration,mpa_id)
VALUES('Крепкий орешек'
        ,'В суперсовременном небоскребе Лос-Анджелеса полицейский Джон Макклейн ведет смертельную схватку с бандой политических террористов'
        ,CAST('1988-07-12' as date)
        ,133
        ,4
);

MERGE INTO film (name,description,releaseDate,duration,mpa_id) KEY(name,description,releaseDate,duration,mpa_id)
    VALUES('Счастливое число Слевина'
    ,'Гангстеры втягивают обычного парня в войну кланов. Виртуозный криминальный триллер с незабываемым финалом'
    ,CAST('2006-04-13' as date)
    ,110
    ,4
    );

MERGE INTO  film_genre (film_id,genre_id) KEY (film_id,genre_id) VALUES (1,6);
MERGE INTO film_genre (film_id,genre_id) KEY (film_id,genre_id) VALUES (1,1);

MERGE INTO  film_genre (film_id,genre_id) KEY (film_id,genre_id) VALUES (2,6);
MERGE INTO film_genre (film_id,genre_id) KEY (film_id,genre_id) VALUES (2,1);
MERGE INTO film_genre (film_id,genre_id) KEY (film_id,genre_id) VALUES (2,4);


MERGE INTO users (email,login,name,birthday) KEY (email,login,name,birthday) VALUES ('test@comp.ru','testuser','Ivanov Ivan Ivanovich', '1982-11-21');
MERGE INTO users (email,login,name,birthday) KEY (email,login,name,birthday) VALUES ('test2@comp.ru','testuser2','Petrov Petr Petrovich', '1990-02-16');

MERGE INTO film_like (film_id,user_id) KEY (film_id,user_id)  VALUES (1,1);
MERGE INTO film_like (film_id,user_id) KEY (film_id,user_id)  VALUES (1,2);


 */
