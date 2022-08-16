package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film implements Comparable<Film> {

    @Builder
    @ConstructorProperties({"id", "name", "description", "releaseDate", "duration", "rate", "genres", "mpa", "likes"})
    public Film(long id, String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration, int rate, Set<Genre> genres, Mpa mpa, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = genres;
        this.mpa = mpa;
        this.likes = likes;
    }

    protected long id;

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

    private int rate;

    private Mpa mpa;

    private Set<Genre> genres;

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
                "id=" + id +
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
        int comp = film.getLikesCount() - this.getLikesCount();
        if (comp == 0) return this.getName().compareTo(film.getName());
        return comp;
    }

    public Set<Genre> getGenres() {
        if (genres == null) {
            return Collections.emptySet();
        } else return new TreeSet<>(genres);
    }


}
