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
public class Film extends Entity implements Comparable<Film> {

    @Builder
    @ConstructorProperties({"id", "name", "description", "releaseDate", "duration", "rate", "genres", "mpa", "likes"})
    public Film(long id, String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration, int rate, Set<Genre> genres, Rating mpa, Set<Long> likes) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = genres;
        this.mpa = mpa;
        this.likes = likes;
    }

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

    private Rating mpa;

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
        int comp = film.getLikesCount() - this.getLikesCount();
        if (comp == 0) return this.getName().compareTo(film.getName());
        return comp;
    }

    public Set<Genre> getGenres() {
        if (genres == null) {
            return Collections.emptySet();
        } else return new TreeSet<>(genres);
    }

    @Getter
    public static class Genre implements Comparable {

        @Positive
        private final Optional<Long> id;
        private Optional<String> name;

        @ConstructorProperties("id")
        public Genre(long id) {
            this.id = Optional.of(id);
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
            return (int) (this.id.orElse(0L) - genre.getId().orElse(0L));
        }
    }

    @Getter
    public static class Rating {

        private final Optional<Long> id;
        private Optional<String> name;

        @ConstructorProperties("id")
        public Rating(long id) {
            this.id = Optional.of(id);
        }


        public Rating(long id, String name) {
            this.id = Optional.of(id);
            this.name = Optional.of(name);
        }
    }


}
