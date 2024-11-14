package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcMpaStorage.class, MpaRowMapper.class})
@DisplayName("Тестирование рейтинга")
public class JdbcMpaRepositoryTest {
    public static final Integer mpaId = 1;
    private final JdbcMpaStorage mpaStorage;

    static Mpa testMpa() {
        Mpa mpa = new Mpa();
        mpa.setId(mpaId);
        mpa.setName("G");
        return mpa;
    }

    static Mpa testMpa2() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        mpa.setName("PG");
        return mpa;
    }

    static List<Mpa> testList = new ArrayList<>(Arrays.asList(testMpa(), testMpa2()));

    @Test
    @DisplayName("Получение рейтинга по id")
    public void getMpaById() {
        Mpa mpa = mpaStorage.getMpaById(mpaId);

        assertThat(mpa)
                .usingRecursiveAssertion()
                .isEqualTo(testMpa());
    }


    @Test
    @DisplayName("Получение всех рейтингов")
    public void getAllMpa() {
        List<Mpa> mpaList = mpaStorage.getMpaList();

        assertThat(mpaList)
                .hasSize(2)
                .isEqualTo(testList);

    }
}
