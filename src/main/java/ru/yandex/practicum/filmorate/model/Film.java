package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film extends Entity implements Comparable<Film> {

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

    private Genre genre;

    private Rating rating;

    @Setter(value = AccessLevel.PRIVATE)
    private Set<Long> likes;

    public Set<Long> getLikes() {
        if (likes == null) {
            return likes = new HashSet<>();
        } else return likes;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }

    public int getLikesCount() {
        if (likes == null) {
            return 0;
        } else return likes.size();

    }

    @Override
    public int compareTo(Film film) {
        int comp =  film.getLikesCount() - this.getLikesCount();
        if (comp == 0) return this.getName().compareTo(film.getName());
        return comp;
    }

}
