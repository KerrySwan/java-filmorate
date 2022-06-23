package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityIsNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Integer, Film> films = new HashMap<>();

    private int filmId;

    public int getNextId() {
        return ++filmId;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            log.error("Film with id: " + id + " does not exist.");
            throw new EntityIsNotValidException("Film with id: " + id + " does not exist.");
        }
    }

    @Override
    public Film addFilm(Film film) {
        EntityValidator.isDateValid(film);
        film.setId(getNextId());
        EntityValidator.throwIfIdIsNotPositive(film);
        films.put(film.getId(), film);
        log.info(film + " added to memory");
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        EntityValidator.throwIfIdIsNotPositive(film);
        EntityValidator.isDateValid(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info(film + " updated");
        } else {
            log.error("Film with id: " + film.getId() + " does not exist.");
            throw new EntityIsNotValidException("Film with id: " + film.getId() + " does not exist.");
        }
        return film;
    }

}
