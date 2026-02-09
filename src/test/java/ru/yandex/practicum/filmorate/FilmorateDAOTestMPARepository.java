package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
public class FilmorateDAOTestMPARepository {
    private final MpaRepository mpaRepository;

    @Test
    public void getAllMpas() {
        List<Mpa> mpas = mpaRepository.getAllMpa();

        assertEquals(5, mpas.size());
    }

    @Test
    public void getMpaById() {
        List<Mpa> mpas = mpaRepository.getAllMpa();

        Optional<Mpa> mpa = mpaRepository.getMpaById(mpas.get(0).getId());

        assertThat(mpa).isPresent();
        assertEquals(mpas.get(0).getName(), mpa.get().getName());

        assertThat(mpaRepository.getMpaById(9999999L)).isEmpty();
    }
}
