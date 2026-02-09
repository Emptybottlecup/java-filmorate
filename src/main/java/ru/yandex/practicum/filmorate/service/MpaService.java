package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public List<Mpa> getAllMpa() {
        return mpaRepository.getAllMpa();
    }

    public Mpa getMpaById(long id) {
        return mpaRepository.getMpaById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.MPA));
    }
}
