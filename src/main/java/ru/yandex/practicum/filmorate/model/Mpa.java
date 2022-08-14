package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.Optional;

@Getter
public class Mpa extends Entity {

    private Optional<String> name;

    @ConstructorProperties("id")
    public Mpa(long id) {
        super.id = Optional.of(id);
    }


    public Mpa(long id, String name) {
        this.id = Optional.of(id);
        this.name = Optional.of(name);
    }
}
