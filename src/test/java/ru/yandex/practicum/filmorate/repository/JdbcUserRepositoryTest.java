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
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcUserStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Проверка DAO пользователей")
public class JdbcUserRepositoryTest {
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
    @DisplayName("Получение пользователя")
    public void getUserById() {
        User user = userStorage.getUserById(1L);
        assertThat(user)
                .usingRecursiveAssertion()
                .isEqualTo(testUser());

    }

    @Test
    @DisplayName("Получение всех пользователей")
    public void getAllUsers() {
        Collection<User> users = userStorage.getAll();
        Collection<User> dbUsers = new ArrayList<>(Arrays.asList(testUser(), testUser2()));
        assertThat(users)
                .hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(dbUsers);
    }

    @Test
    @DisplayName("Сохранение пользователя")
    public void saveUser() {
        User user = userStorage.save(testSaveUser());
        User savedUser = testSaveUser();
        savedUser.setId(3L);
        assertThat(user)
                .usingRecursiveAssertion()
                .isEqualTo(savedUser);

        Collection<User> allBdUsers = userStorage.getAll();
        assertThat(allBdUsers)
                .hasSize(3);

    }

    @Test
    @DisplayName("Обновление пользователя")
    public void updateUser() {
        User updatedUser = testUser2();
        updatedUser.setName("New Update Name User");
        updatedUser.setLogin("newUpdateLogin");
        updatedUser.setName("updated@email.com");
        updatedUser.setBirthday(LocalDate.of(2024, 11, 15));

        User user = userStorage.update(updatedUser);
        assertThat(user)
                .usingRecursiveAssertion()
                .isEqualTo(updatedUser);
    }

    @Test
    @DisplayName("Добавить друга")
    public void addFriend() {
        userStorage.addFriend(testUser(), testUser2());
        List<User> savedFriends = userStorage.getUserFriends(testUser());

        assertThat(savedFriends)
                .hasSize(1)
                .usingRecursiveAssertion()
                .isEqualTo(new ArrayList<>(Arrays.asList(testUser2())));
    }

    @Test
    @DisplayName("Удалить друга")
    public void removeFriend() {
        userStorage.addFriend(testUser(), testUser2());
        User testUser3 = userStorage.save(testSaveUser());
        userStorage.addFriend(testUser(), testUser3);
        List<User> savedFriends = userStorage.getUserFriends(testUser());

        assertThat(savedFriends)
                .hasSize(2);

        userStorage.removeFriend(testUser(), testUser2());
        List<User> friendsAfterRemove = userStorage.getUserFriends(testUser());

        assertThat(friendsAfterRemove)
                .hasSize(1)
                .usingRecursiveAssertion()
                .isEqualTo(new ArrayList<>(Arrays.asList(testUser3)));

    }

}
