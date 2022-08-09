DROP ALL OBJECTS;

CREATE TYPE IF NOT EXISTS GENRE as ENUM('COMEDY','DRAMA','CARTOON','THRILLER','DOCUMENTARY','ACTION');

CREATE TYPE IF NOT EXISTS RATING as ENUM('G','PG','PG_13','R','NC_17');

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
	rating RATING
);

CREATE TABLE IF NOT EXISTS genresToFilm(
    film_id INTEGER,
    genre GENRE
)

CREATE TABLE IF NOT EXISTS friendship(
	user_id bigint,
	friend_id bigint,
	is_accepted boolean
);

CREATE TABLE IF NOT EXISTS likes(
	film_id bigint ,
	user_id bigint
);
