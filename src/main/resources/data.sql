MERGE INTO mpas (name) 
KEY(name) 
VALUES ('G');

MERGE INTO mpas (name)
KEY(name)
VALUES ('PG');

MERGE INTO mpas (name)
KEY(name)
VALUES ('PG-13');

MERGE INTO mpas (name)
KEY (name)
VALUES ('R');

MERGE INTO mpas (name)
KEY (name)
VALUES ('NC-17');

MERGE INTO genres (name)
KEY (name)
VALUES ('Комедия');

MERGE INTO genres (name)
KEY (name)
VALUES ('Драма');

MERGE INTO genres (name)
KEY (name)
VALUES ('Мультфильм');

MERGE INTO genres (name)
KEY (name)
VALUES ('Триллер');

MERGE INTO genres (name)
KEY (name)
VALUES ('Документальный');

MERGE INTO genres (name)
KEY (name)
VALUES ('Боевик');