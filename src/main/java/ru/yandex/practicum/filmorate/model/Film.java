package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film extends Entity{

    @NotBlank
    private final String name;
    @NonNull
    @Size(max = 200, message = "Description name longer than 200 symbols")
    private final String description;
    @NonNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate releaseDate;
    @NonNull
    @Positive
    private int duration;

    @Override
    public String toString() {
        return "Film{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }

    
}
