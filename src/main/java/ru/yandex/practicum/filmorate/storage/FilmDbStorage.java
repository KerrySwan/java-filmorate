package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.type.Genre;
import ru.yandex.practicum.filmorate.model.type.Rating;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long filmId;

    public long getNextId() {
        return ++filmId;
    }

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
        SqlRowSet likes = jdbcTemplate.queryForRowSet("select film_id, user_id from likes where film_id = ?", film.getId());
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
                    .genres(Genre.getCodeFromGenre(filmsRows.getString("genre")))
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
        if (checkForFilmExistence(film)) {
            log.error("Film with id: " + film.getId() + " already exists.");
            throw new EntityAlreadyExistException("Film with id: " + film.getId() + " already exists.");
        }
        EntityValidator.isDateValid(film);
        film.setId(getNextId());
        jdbcTemplate.update(
                "INSERT INTO films (id, name, description, release, duration, genre, rating) VALUES (?, ?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre(),
                Rating.getRating(film.getMpa().getId())
        );
        addLikes(film);
        return film;
    }

    private void addLikes(Film film) {
        Set<Long> usersId = film.getLikes();
        for (Long id : usersId) {
            jdbcTemplate.update(
                    "INSERT INTO likes (film_id, user_id) values (?, ?)",
                    film.getId(),
                    id
            );
        }
    }

    private void removeLikes(Film film) {
        jdbcTemplate.update(
                "DELETE FROM likes WHERE film_id = ?",
                film.getId()
        );
    }

    @Override
    public Film putFilm(Film film) {
        if (!checkForFilmExistence(film)) {
            log.error("Film with id: " + film.getId() + " does not exist.");
            throw new EntityIsNotFoundException("Film with id: " + film.getId() + " does not exist.");
        }
        EntityValidator.isDateValid(film);
        jdbcTemplate.update(
                "UPDATE films SET name = ?, description = ?, release = ?, duration = ?, genre = ?, rating = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre(),
                Rating.getRating(film.getMpa().getId()),
                film.getId()
        );
        removeLikes(film);
        addLikes(film);
        return film;
    }

    private boolean checkForFilmExistence(Film film) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("select id from films where id = ?", film.getId());
        if (row.next()) {
            return row.getLong("id") == film.getId();
        }
        return false;
    }

    private
}
