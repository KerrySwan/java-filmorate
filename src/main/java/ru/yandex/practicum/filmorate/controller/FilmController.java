package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.sevice.FilmService;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Component
@RequiredArgsConstructor
@Validated
public class FilmController {

    final private FilmService service;

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return service.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable(name = "id") @Positive int filmId) {
        return service.getFilm(filmId);
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody @Valid Film film) {
        return service.addFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody @Valid Film film) {
        return service.putFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") @Positive long filmId, @PathVariable @Positive long userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") @Positive long filmId, @PathVariable @Positive long userId) {
        service.removeLike(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public Set<Film> getPopular(@RequestParam(required = false, defaultValue = "10") @Positive int count) {
        return service.getOrderedSet(count);
    }

}
