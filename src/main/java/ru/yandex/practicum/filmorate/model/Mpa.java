package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.Optional;

@Getter
@Setter
public class Mpa {

    protected long id;

    private Optional<String> name;

    @ConstructorProperties("id")
    public Mpa(long id) {
        this.id = id;
    }


    public Mpa(long id, String name) {
        this.id = id;
        this.name = Optional.of(name);
    }
}
