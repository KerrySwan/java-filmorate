package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EntityIsNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.EntityValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();

    private int filmId;

    public int getNextId() {
        return ++filmId;
    }


    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        EntityValidator.isDateValid(film);
        film.setId(getNextId());
        EntityValidator.throwIfIdIsNotPositive(film);
        films.put(film.getId(), film);
        log.info(film + " added to memory");
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody @Valid Film film) {
        EntityValidator.throwIfIdIsNotPositive(film);
        EntityValidator.isDateValid(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info(film + " updated");
        } else {
            log.warn(film + " does not exist");
        }

        return film;
    }

}
