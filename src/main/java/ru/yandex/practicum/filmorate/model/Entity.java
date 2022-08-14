package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Entity {

    protected Optional<Long> id;

    public Long getId() {
        return id.orElse(null);
    }
}
