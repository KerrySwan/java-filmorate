package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    private final String name;
    private final String description;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate releaseDate;
    @NonNull
    private Duration duration;

}
