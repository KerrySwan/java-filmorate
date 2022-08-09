package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Entity {

    protected long id;

}
