package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.Optional;

@Getter
public class Genre extends Entity implements Comparable {

    private Optional<String> name;

    @ConstructorProperties("id")
    public Genre(long id) {
        super.id = Optional.of(id);
    }

    public Genre(long id, String name) {
        this.id = Optional.of(id);
        this.name = Optional.of(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public int compareTo(Object o) {
        Genre genre = (Genre) o;
        return (int) (this.id.orElse(0L) - genre.getId());
    }
}
