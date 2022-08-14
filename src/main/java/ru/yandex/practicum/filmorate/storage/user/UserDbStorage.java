package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired @Qualifier("friendDbStorage")
    FriendStorage friendStorage;

    private long userId;

    public UserDbStorage(@Autowired JdbcTemplate jdbcTemplate, @Autowired @Qualifier("friendDbStorage") FriendStorage friendStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendStorage = friendStorage;
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
        friendStorage.getFriends(users);
        return users.values();
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
            friendStorage.getFriends(user);
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
        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(Optional.of(getLastId()));
        friendStorage.addFriends(user);
        return getUser(getLastId());
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
        friendStorage.removeFriends(user);
        friendStorage.addFriends(user);
        return user;
    }

    private boolean checkForUserExistence(User user) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("select id from users where id = ?", user.getId());
        if (row.next()) {
            return row.getLong("id") == user.getId();
        }
        return false;
    }

    private long getLastId(){
        SqlRowSet row = jdbcTemplate.queryForRowSet("select max(id) as id from users");
        if (row.next()) {
            return row.getLong("id");
        }
        return -1;
    }
}
