package ru.yandex.practicum.filmorate.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.model.films.Mpa;

@UtilityClass
public class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        MpaDto mpaDto = new MpaDto();

        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());

        return mpaDto;
    }
}
