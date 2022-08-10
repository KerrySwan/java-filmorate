package ru.yandex.practicum.filmorate.model.type;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

@Data
public class Genre {

    @Positive
    private final long id;

}
