package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.sevice.MpaService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Component
@RequiredArgsConstructor
@Validated
public class MpaController {

    final private MpaService service;

    @GetMapping(value = "/mpa")
    public List<Mpa> getMpa() {
        return service.getMpa();
    }

    @GetMapping(value = "/mpa/{id}")
    public Mpa getMpa(@PathVariable @Positive long id) {
        return service.getMpa(id);
    }

}
