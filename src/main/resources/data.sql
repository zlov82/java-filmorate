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


MERGE INTO film (name,description,releaseDate,duration,mpa_id) KEY(name,description,releaseDate,duration,mpa_id)
VALUES('Крепкий орешек'
        ,'В суперсовременном небоскребе Лос-Анджелеса полицейский Джон Макклейн ведет смертельную схватку с бандой политических террористов, взявших в заложники два десятка человек, в число которых попадает и его жена. Началось все с того, что парень приехал в город к жене, оказался на рождественском приеме, а кончилось настоящей войной'
        ,CAST('1988-07-12' as date)
        ,133
        ,4
);




