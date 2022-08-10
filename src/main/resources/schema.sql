DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS users(
	id bigint PRIMARY KEY,
	email varchar(255),
	login varchar(255),
	name varchar(255),
	birthday date
);

CREATE TABLE IF NOT EXISTS films(
	id bigint PRIMARY KEY,
	name varchar(255),
	description varchar(255),
	release date,
	duration integer,
	rating_id bigint
);

CREATE TABLE IF NOT EXISTS filmGenres(
    film_id bigint,
    genre_id bigint
);

CREATE TABLE IF NOT EXISTS friendship(
	user_id bigint,
	friend_id bigint,
	is_accepted boolean
);

CREATE TABLE IF NOT EXISTS likes(
	film_id bigint ,
	user_id bigint
);

CREATE TABLE IF NOT EXISTS genres(
  id bigint,
  genre varchar(255)
);

CREATE TABLE IF NOT EXISTS ratings(
  id bigint,
  rating varchar(255)
);