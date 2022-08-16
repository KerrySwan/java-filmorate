package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.util.Map;

@Component
public class FriendDbStorage implements FriendStorage {


    JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void getFriends(Map<Long, User> users) {
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

    @Override
    public void getFriends(User user) {
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
    public void addFriends(User user) {
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

    @Override
    public void removeFriends(User user) {
        jdbcTemplate.update(
                "DELETE FROM friendship WHERE user_id = ?",
                user.getId()
        );
    }

}
