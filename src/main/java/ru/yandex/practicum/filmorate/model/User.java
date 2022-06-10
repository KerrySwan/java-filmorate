package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class User extends Entity {
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


    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

}
