package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.type.Genre;
import ru.yandex.practicum.filmorate.model.type.Rating;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getFilms() {
        Map<Long, Film> films = new HashMap<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("select id, name, description, release, duration, genre, rating from films");
        while (filmsRows.next()) {
            long film_id = filmsRows.getLong("id");
            Film film = Film.builder()
                    .id(film_id)
                    .name(filmsRows.getString("name"))
                    .description(filmsRows.getString("description"))
                    .releaseDate(filmsRows.getDate("release").toLocalDate())
                    .duration(filmsRows.getInt("duration"))
                    .genre(Genre.valueOf(filmsRows.getString("genre")))
                    .rating(Rating.valueOf(filmsRows.getString("rating")))
                    .build();
            films.put(film_id, film);
        }
        getLikes(films);
        return films.values();
    }

    private void getLikes(Map<Long, Film> films) {
        SqlRowSet likes = jdbcTemplate.queryForRowSet("select film_id, user_id from likes");
        while (likes.next()) {
            long film_id = likes.getLong("film_id");
            if (films.containsKey(film_id)) {
                films.get(film_id)
                        .getLikes()
                        .add(likes.getLong("user_id"));
            }
        }
    }

    private void getLikes(Film film) {
        SqlRowSet likes = jdbcTemplate.queryForRowSet("select film_id, user_id from likes where id = ?", film.getId());
        if (likes.next()) {
            film.getLikes().add(likes.getLong("user_id"));
        }
    }

    @Override
    public Film getFilm(long id) {
        SqlRowSet filmsRows =
                jdbcTemplate.queryForRowSet("select id, name, description, release, duration, genre, rating from films where id = ?", id);
        if (filmsRows.next()) {
            Film film = Film.builder()
                    .id(filmsRows.getLong("id"))
                    .name(filmsRows.getString("name"))
                    .description(filmsRows.getString("description"))
                    .releaseDate(filmsRows.getDate("release").toLocalDate())
                    .duration(filmsRows.getInt("duration"))
                    .genre(Genre.valueOf(filmsRows.getString("genre")))
                    .rating(Rating.valueOf(filmsRows.getString("rating")))
                    .build();
            getLikes(film);
            return film;
        }
        log.error("Film with id: " + id + " does not exist.");
        throw new EntityIsNotFoundException("Film with id: " + id + " does not exist.");
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(
                "INSERT INTO TABLE films (id, name, description, release, duration, genre, rating) VALUES (?, ?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre().toString(),
                film.getRating().toString()
        );
        addLikes(film);
        return film;
    }

    private void addLikes(Film film) {
        Set<Long> usersId = film.getLikes();
        for (Long id : usersId) {
            jdbcTemplate.update(
                    "INSERT INTO TABLE likes (film_id, user_id) values (?, ?)",
                    film.getId(),
                    id
            );
        }
    }

    private void removeLikes(Film film) {
        jdbcTemplate.update(
                "DELETE FROM TABLE likes WHERE film_id = ?",
                film.getId()
        );
    }

    @Override
    public Film putFilm(Film film) {
        jdbcTemplate.update(
                "UPDATE TABLE films name = ?, description = ?, release = ?, duration = ?, genre = ?, rating = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre().toString(),
                film.getRating().toString(),
                film.getId()
        );
        removeLikes(film);
        addLikes(film);
        return film;
    }

}
