package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAllMpa() {
        log.info("Запрос всех рейтингов");
        List<Mpa> mpaList = mpaService.getAllMpa();
        log.info("Ответ на запрос всех рейтигов:\n{}", mpaList);
        return mpaList;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        log.info("Запрос рейтинга по id = {}", id);
        Mpa mpa = mpaService.getMpaById(id);
        log.info("Ответ запроса вывода рейтинга:\n{}", mpa);
        return mpa;
    }
}
