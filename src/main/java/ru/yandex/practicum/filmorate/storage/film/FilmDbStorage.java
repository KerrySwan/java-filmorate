package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private GenreStorage genreStorage;

    private MpaStorage mpaStorage;

    private LikeStorage likeStorage;

    @Autowired
    public FilmDbStorage( JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage, LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public Collection<Film> getFilms() {
        Map<Long, Film> films = new HashMap<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("select id, name, description, release, duration, rating_id from films");

        while (filmsRows.next()) {
            long film_id = filmsRows.getLong("id");
            Film film = Film.builder()
                    .id(film_id)
                    .name(filmsRows.getString("name"))
                    .description(filmsRows.getString("description"))
                    .releaseDate(filmsRows.getDate("release").toLocalDate())
                    .duration(filmsRows.getInt("duration"))
                    .genres(genreStorage.getGenresForFilm(film_id))
                    .mpa(mpaStorage.getMpa(filmsRows.getLong("rating_id")))
                    .build();
            films.put(film_id, film);
        }
        likeStorage.getLikes(films);
        return films.values();
    }


    @Override
    public Film getFilm(long id) {
        SqlRowSet filmsRows =
                jdbcTemplate.queryForRowSet("select id, name, description, release, duration, rating_id from films where id = ?", id);
        if (filmsRows.next()) {
            Film film = Film.builder()
                    .id(filmsRows.getLong("id"))
                    .name(filmsRows.getString("name"))
                    .description(filmsRows.getString("description"))
                    .releaseDate(filmsRows.getDate("release").toLocalDate())
                    .duration(filmsRows.getInt("duration"))
                    .genres(genreStorage.getGenresForFilm(filmsRows.getLong("id")))
                    .mpa(mpaStorage.getMpa(filmsRows.getLong("rating_id")))
                    .build();
            likeStorage.getLikes(film);
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

        String sql = "INSERT INTO films (name, description, release, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        Object[] types = new Object[]{film.getName(), film.getDescription(), film.getReleaseDate().toString(),
                                      film.getDuration(), film.getMpa().getId()};
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
                sql,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT
        );
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
        PreparedStatementCreator ps = preparedStatementCreatorFactory.newPreparedStatementCreator(types);
        jdbcTemplate.update(ps, keyHolder);
        long id = (long) keyHolder.getKey();

        film.setId(id);
        genreStorage.updateGenreForFilm(film);
        likeStorage.addLikes(film);
        return film;
    }


    @Override
    public Film putFilm(Film film) {
        if (!checkForFilmExistence(film)) {
            log.error("Film with id: " + film.getId() + " does not exist.");
            throw new EntityIsNotFoundException("Film with id: " + film.getId() + " does not exist.");
        }
        EntityValidator.isFilmValid(film);
        jdbcTemplate.update(
                "UPDATE films SET name = ?, description = ?, release = ?, duration = ?, rating_id = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        genreStorage.updateGenreForFilm(film);
        likeStorage.addLikes(film);
        return film;
    }

    private boolean checkForFilmExistence(Film film) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("select id from films where id = ?", film.getId());
        if (row.next()) {
            return row.getLong("id") == film.getId();
        }
        return false;
    }


}
