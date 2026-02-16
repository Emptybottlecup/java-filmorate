package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public List<MpaDto> getAllMpa() {
        return mpaRepository.getAllMpa().stream()
                .map(MpaMapper::mapToMpaDto).toList();
    }

    public MpaDto getMpaById(long id) {
        Mpa mpa = mpaRepository.getMpaById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.MPA));
        return MpaMapper.mapToMpaDto(mpa);
    }
}
