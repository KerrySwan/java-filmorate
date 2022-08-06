package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select id, email, login, name, birthday from users");
        while (userRows.next()) {
            users.put(
                    userRows.getLong("id"),
                    User.builder()
                            .id(userRows.getLong("id"))
                            .email(userRows.getString("email"))
                            .login(userRows.getString("login"))
                            .name(userRows.getString("name"))
                            .birthday(userRows.getDate("birthday").toLocalDate())
                            .build()
            );
        }
        getFriends(users);
        return users.values();
    }

    private void getFriends(Map<Long, User> users) {
        SqlRowSet friendshipRows = jdbcTemplate.queryForRowSet("select user_id, friend_id, is_accepted from friendship");
        while (friendshipRows.next()) {
            long id = friendshipRows.getLong("user_id");
            if (users.containsKey(id)) {
                users.get(id)
                        .getFriends()
                        .put(
                                friendshipRows.getLong("friend_id"),
                                friendshipRows.getBoolean("is_accepted")
                        );
            }
        }
    }

    private void getFriends(User user) {
        SqlRowSet friendshipRows = jdbcTemplate.queryForRowSet("select user_id, friend_id, is_accepted from friendship where user_id = ?", user.getId());
        while (friendshipRows.next()) {
            long id = friendshipRows.getLong("user_id");
            user.getFriends()
                    .put(
                            friendshipRows.getLong("friend_id"),
                            friendshipRows.getBoolean("is_accepted")
                    );
        }
    }

    @Override
    public User getUser(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select id, email, login, name, birthday from users where id = ?", id);
        if (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getLong("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build();
            getFriends(user);
            return user;
        }
        log.error("User with id: " + id + " does not exist.");
        throw new EntityIsNotFoundException("User with id: " + id + " does not exist.");
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(
                "INSERT INTO TABLE users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        addFriends(user);
        return user;
    }

    public void addFriends(User user) {
        for (Map.Entry<Long, Boolean> f : user.getFriends().entrySet()) {
            jdbcTemplate.update(
                    "INSERT INTO TABLE friendship (user_id, friend_id, is_accepted) values (?, ?, ?)",
                    user.getId(),
                    f.getKey(),
                    f.getValue()
            );
        }
    }

    private void removeFriends(User user) {
        jdbcTemplate.update(
                "DELETE FROM TABLE friendship WHERE user_id = ?",
                user.getId()
        );
    }

    @Override
    public User putUser(User user) {
        jdbcTemplate.update(
                "UPDATE TABLE users email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        removeFriends(user);
        addFriends(user);
        return user;
    }
}
