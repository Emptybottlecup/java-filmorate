package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.LikeDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateInformation;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreAndFilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final UserService userService;
    private final LikeService likeService;

    public List<FilmDto> getAllFilms() {
        Map<Long, MpaDto> mpaIdAndMpaDto = mpaService.getAllMpa().stream()
                .collect(Collectors.toMap(MpaDto::getId, mpaDto -> mpaDto));

        Map<Long, GenreDto> genreIdAndGenreDto = genreService.getAllGenres().stream()
                .collect(Collectors.toMap(GenreDto::getId, genreDto -> genreDto));

        Map<Long, List<GenreDto>> filmsIdAndGenresDto = genreService.getAllGenresAndFilms().stream()
                .collect(Collectors.groupingBy(GenreAndFilmDto::getIdFilm, Collectors.mapping(
                        genreAndFilmDto -> genreIdAndGenreDto.get(genreAndFilmDto.getIdGenre()),
                        Collectors.toList())));

        return filmStorage.getAllFilms().stream()
                .map(film -> {
            List<GenreDto> genresDto = filmsIdAndGenresDto.getOrDefault(film.getId(), new ArrayList<>());
            MpaDto mpaDto = mpaIdAndMpaDto.get(film.getIdMpa());
            return FilmMapper.mapToFilmDto(film, mpaDto, genresDto);})
                .toList();
    }

    public FilmDto getFilmById(long id) {
        Film film = filmStorage.getFilmById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.FILM));

        MpaDto mpaDto = mpaService.getMpaById(film.getIdMpa());

        List<GenreDto> genresDto = genreService.getGenreOfFilmById(film.getId());

        return FilmMapper.mapToFilmDto(film, mpaDto, genresDto);
    }

    public FilmDto addFilm(NewFilmRequest newFilmRequest) {

        if (newFilmRequest.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма должна быть позже 28.12.1895");
        }

        MpaDto mpa = mpaService.getMpaById(newFilmRequest.getMpa().getId());

        Film film = filmStorage.addNewFilm(FilmMapper.createFilm(newFilmRequest)).orElseThrow(() -> new InternalServerException("Не получилось создать фильм"));

        List<GenreDto> genres = genreService.addGenresOfFilm(film.getId(), newFilmRequest.getGenres());

        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }

    public List<FilmDto> getPopularFilms(int count) {
        if (count < 0) {
            log.warn("Неверное количество выводимых фильмов");
            throw new ConditionsNotMetException("Отсчет не может начинаться с отрицательного числа", "count");
        }
        return getAllFilms().stream().sorted(Comparator.comparingInt((filmDto) -> likeService.getLikesOfFilm(filmDto.getId()).size() * -1)).limit(count).toList();
    }

    public void deleteLike(Long filmId, Long userId) {
        FilmDto filmDto = getFilmById(filmId);
        UserDto userDto = userService.getUser(userId);

        likeService.deleteLike(filmId, userId);

        log.debug("Был удален лайк у фильма {} пользователя {}", filmDto.getName(), userDto.getLogin());
    }

    public LikeDto addLike(Long filmId, Long userId) {
        FilmDto filmDto = getFilmById(filmId);
        UserDto user = userService.getUser(userId);
        LikeDto likeDto = likeService.addLike(filmId, userId);
        log.debug("Был добавлен лайк фильму {} пользователем {}", filmDto.getName(), user.getLogin());
        return likeDto;
    }


    public FilmDto putFilm(FilmUpdateInformation filmUpdateInformation) {
        if (filmUpdateInformation.hasMpa()) {
            mpaService.getMpaById(filmUpdateInformation.getNewMpaRequest().getId());
        }

        Film film = filmStorage.getFilmById(filmUpdateInformation.getId()).orElseThrow(() -> new NotFoundIdException(filmUpdateInformation.getId(), WhichObjectNotFound.FILM));


        MpaDto mpaDto = mpaService.getMpaById(film.getIdMpa());

        List<GenreDto> genresDto;
        if (filmUpdateInformation.hasGenres()) {
            genreService.deleteGenreOfFilm(film.getId());
            genresDto = genreService.addGenresOfFilm(film.getId(), filmUpdateInformation.getNewGenreRequests());
        } else {
            genresDto = genreService.getGenreOfFilmById(film.getId());
        }

        FilmMapper.updateFilmInformation(film, filmUpdateInformation);

        filmStorage.updateFilmInformation(film).orElseThrow(() -> new InternalServerException("Не получилось обновить данные фильма"));


        return FilmMapper.mapToFilmDto(film, mpaDto, genresDto);
    }
}
