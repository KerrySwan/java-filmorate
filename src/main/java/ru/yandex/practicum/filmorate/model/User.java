package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Builder
    public User(long id, @NotBlank String email, @NotBlank String login, String name, @NonNull LocalDate birthday, Map<Long, Boolean> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
    }

    protected long id;

    @Email(message = "E-mail must be declared properly")
    @NotBlank(message = "E-mail must not be empty or blank")
    private String email;
    @NotBlank(message = "Login must not be empty or blank")
    @Pattern(regexp = "\\w+", message = "Login must be declared properly")
    private String login;
    private String name;

    @NonNull
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /***
     * If one user had sent addFriend request to another, both of them got befriended with FALSE boolean status.
     * This status means that second user had not sent approval with same request.
     * After sending request from both of them status gets updated with TRUE.
     * This can be useful to create some kind of 'Follower' status.
     * */
    @Setter(value = AccessLevel.PRIVATE)
    private Map<Long, Boolean> friends;

    public Map<Long, Boolean> getFriends() {
        if (friends == null) {
            return friends = new HashMap<>();
        } else return friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

}
