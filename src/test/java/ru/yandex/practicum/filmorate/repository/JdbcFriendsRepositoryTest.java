package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Добавление/удаление из друзей")
@Import({JdbcFriendsStorage.class, JdbcUserStorage.class, UserRowMapper.class})
public class JdbcFriendsRepositoryTest {
    private final JdbcFriendsStorage friendsStorage;
    private final JdbcUserStorage userStorage;

    public static User testUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Ivanov Ivan Ivanovich");
        user.setLogin("testuser");
        user.setEmail("test@comp.ru");
        user.setBirthday(LocalDate.of(1982, 11, 21));
        return user;
    }

    public static User testUser2() {
        User user = new User();
        user.setId(2L);
        user.setName("Petrov Petr Petrovich");
        user.setLogin("testuser2");
        user.setEmail("test2@comp.ru");
        user.setBirthday(LocalDate.of(1990, 02, 16));
        return user;
    }

    public static User testSaveUser() {
        User user = new User();
        user.setName("User To Save");
        user.setLogin("newUser");
        user.setEmail("newuser@newuser.com");
        user.setBirthday(LocalDate.of(2000, 11, 02));
        return user;
    }


    @Test
    @DisplayName("Добавить друга")
    public void addFriend() {
        friendsStorage.addFriend(testUser(), testUser2());
        List<User> savedFriends = userStorage.getUserFriends(testUser());

        assertThat(savedFriends)
                .hasSize(1)
                .usingRecursiveAssertion()
                .isEqualTo(new ArrayList<>(Arrays.asList(testUser2())));
    }

    @Test
    @DisplayName("Удалить друга")
    public void removeFriend() {
        friendsStorage.addFriend(testUser(), testUser2());
        User testUser3 = userStorage.save(testSaveUser());
        friendsStorage.addFriend(testUser(), testUser3);
        List<User> savedFriends = userStorage.getUserFriends(testUser());

        assertThat(savedFriends)
                .hasSize(2);

        friendsStorage.removeFriend(testUser(), testUser2());
        List<User> friendsAfterRemove = userStorage.getUserFriends(testUser());

        assertThat(friendsAfterRemove)
                .hasSize(1)
                .usingRecursiveAssertion()
                .isEqualTo(new ArrayList<>(Arrays.asList(testUser3)));

    }
}
