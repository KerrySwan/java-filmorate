package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.sevice.GenreService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Component
@RequiredArgsConstructor
@Validated
public class GenreController {

    final private GenreService service;

    @GetMapping(value = "/genres")
    public List<Genre> getGenres() {
        return service.getGenres();
    }

    @GetMapping(value = "/genres/{id}")
    public Genre getGenre(@PathVariable @Positive long id) {
        return service.getGenre(id);
    }

}
