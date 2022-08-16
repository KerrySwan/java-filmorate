package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.sevice.LikeService;

import javax.validation.constraints.Positive;

@RestController
@Component
@RequiredArgsConstructor
@Validated
public class LikeController {

    final private LikeService service;

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") @Positive long filmId, @PathVariable @Positive long userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") @Positive long filmId, @PathVariable @Positive long userId) {
        service.removeLike(filmId, userId);
    }
}
