# Проект Filmorate
## Схема базы данных проекта Filmorate.
![](https://github.com/Emptybottlecup/java-filmorate/blob/main/database_scheme.png)
# Основная информация о базе данных проекта.
1. Примеры основных запросов SQL:
  Получение всех фильмов
  SELECT *
  FROM films;

  Получение всех пользоватлей
  SELECT *
  FROM users;

  Получение 10 наименований наиболее популярных фильмов в порядке убывания лайков
  SELECT films.name,
         COUNT(likes.id_user) AS likes_count
  FROM films LEFT JOIN likes ON likes.id_film = films.id
  GROUP BY films.name
  ORDER BY likes_count DESC
  LIMIT 10;

  Получение общих друзей двух пользователей
  SELECT u.name, u.email
  FROM users AS u
  JOIN (SELECT id_friend_user
        FROM friends
        WHERE is_confirmed = true AND id_user = 1) AS f1 ON u.id = f1.id_friend_user
  JOIN (SELECT id_friend_user
        FROM friends
        WHERE is_confirmed = true AND id_user = 2) AS f2 ON u.id = f2.id_friend_user;
2. 

