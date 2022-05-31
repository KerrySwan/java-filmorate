package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;



}
