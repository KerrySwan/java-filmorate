package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.util.*;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long userId;

    public long getNextId() {
        SqlRowSet userIds = jdbcTemplate.queryForRowSet("select id from users");
        List<Long> idList = new ArrayList<>();
        while(userIds.next()){
            idList.add(userIds.getLong("id"));
        }
        long id = Entity.findMissingId(idList, userId);
        return id == -1 ? ++userId : id;
    }

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
        if (checkForUserExistence(user)) {
            log.error("User with id: " + user.getId() + " already exists.");
            throw new EntityAlreadyExistException("User with id: " + user.getId() + " already exists.");
        }
        if (!EntityValidator.isUserNameValid(user)) user.setName(user.getLogin());
        user.setId(getNextId());
        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        addFriends(user);
        return user;
    }

    private void addFriends(User user) {
        if (!EntityValidator.isUserNameValid(user)) user.setName(user.getLogin());
        for (Map.Entry<Long, Boolean> f : user.getFriends().entrySet()) {
            jdbcTemplate.update(
                    "INSERT INTO friendship (user_id, friend_id, is_accepted) values (?, ?, ?)",
                    user.getId(),
                    f.getKey(),
                    f.getValue()
            );
        }
    }

    private void removeFriends(User user) {
        jdbcTemplate.update(
                "DELETE FROM friendship WHERE user_id = ?",
                user.getId()
        );
    }

    @Override
    public User putUser(User user) {
        if (!checkForUserExistence(user)) {
            log.error("User with id: " + user.getId() + " does not exist.");
            throw new EntityIsNotFoundException("User with id: " + user.getId() + " does not exist.");
        }
        if (!EntityValidator.isUserNameValid(user)) user.setName(user.getLogin());
        jdbcTemplate.update(
                "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
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

    private boolean checkForUserExistence(User user){
        SqlRowSet row = jdbcTemplate.queryForRowSet("select id from users where id = ?", user.getId());
        if (row.next()){
            return row.getLong("id") == user.getId();
        }
        return false;
    }
}
