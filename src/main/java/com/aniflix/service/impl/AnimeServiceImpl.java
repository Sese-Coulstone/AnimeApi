package com.aniflix.service.impl;

import com.aniflix.dto.AnimeDto;
import com.aniflix.dto.AnimePage;
import com.aniflix.entity.Anime;
import com.aniflix.exceptions.AnimeNotFoundException;
import com.aniflix.exceptions.FileExistsException;
import com.aniflix.repository.AnimeRepository;
import com.aniflix.service.AnimeService;
import com.aniflix.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnimeServiceImpl implements AnimeService {
    @Value("${poster}")
    private String path;

    @Value("${url}")
    private String baseUrl;

    private final AnimeRepository animeRepository;
    private final FileService fileService;

    public AnimeServiceImpl(AnimeRepository animeRepository, FileService fileService) {
        this.animeRepository = animeRepository;
        this.fileService = fileService;
    }

    @Override
    public AnimeDto addAnime(MultipartFile file, AnimeDto animeDto) throws IOException {
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileExistsException("File already exists: " + file.getOriginalFilename());
        }

        String uploadedFile = fileService.uploadFile(path, file);

        animeDto.setPoster(uploadedFile);

        Anime anime = new Anime(
                null,
                animeDto.getTitle(),
                animeDto.getDirector(),
                animeDto.getStudio(),
                animeDto.getAnimeCast(),
                animeDto.getReleaseYear(),
                animeDto.getPoster()
        );
        Anime saved = animeRepository.save(anime);

        String posterUrl = baseUrl + "/file/" + uploadedFile;

        return new AnimeDto(
                saved.getAnimeId(),
                saved.getTitle(),
                saved.getDirector(),
                saved.getStudio(),
                saved.getAnimeCast(),
                saved.getReleaseYear(),
                saved.getPoster(),
                posterUrl
        );
    }

    @Override
    public AnimeDto getAnime(Integer id) {
        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with id: " + id));
        String posterUrl = baseUrl + "/file/" + anime.getPoster();
        return new AnimeDto(
                anime.getAnimeId(),
                anime.getTitle(),
                anime.getDirector(),
                anime.getStudio(),
                anime.getAnimeCast(),
                anime.getReleaseYear(),
                anime.getPoster(),
                posterUrl
        );
    }

    @Override
    public AnimeDto updateAnime(Integer id, MultipartFile file, AnimeDto animeDto) throws IOException {
        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with id: " + id));
        String location = path + File.separator + anime.getPoster();
        Files.deleteIfExists(Paths.get(location));
        String uploadedFile = fileService.uploadFile(path, file);
        anime.setPoster(uploadedFile);
        anime.setTitle(animeDto.getTitle());
        anime.setDirector(animeDto.getDirector());
        anime.setStudio(animeDto.getStudio());
        anime.setAnimeCast(animeDto.getAnimeCast());
        anime.setReleaseYear(animeDto.getReleaseYear());
        Anime updatedAnime = animeRepository.save(anime);

        String posterUrl = baseUrl + "/file/" + uploadedFile;
        return new AnimeDto(
                updatedAnime.getAnimeId(),
                updatedAnime.getTitle(),
                updatedAnime.getDirector(),
                updatedAnime.getStudio(),
                updatedAnime.getAnimeCast(),
                updatedAnime.getReleaseYear(),
                updatedAnime.getPoster(),
                posterUrl
        );
    }

    @Override
    public void deleteAnime(Integer id) throws IOException {
        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with id: " + id));
        String location = path + File.separator + anime.getPoster();
        Files.deleteIfExists(Paths.get(location));
        animeRepository.delete(anime);
    }

    @Override
    public List<AnimeDto> getAllAnime() {
        List<Anime> animeList = animeRepository.findAll();

        List<AnimeDto> animeDtoList = new ArrayList<>();

        for (Anime anime : animeList) {
            String posterUrl = baseUrl + "/file/" + anime.getPoster();
            AnimeDto animeDto = new AnimeDto(
                    anime.getAnimeId(),
                    anime.getTitle(),
                    anime.getDirector(),
                    anime.getStudio(),
                    anime.getAnimeCast(),
                    anime.getReleaseYear(),
                    anime.getPoster(),
                    posterUrl
            );
            animeDtoList.add(animeDto);
        }

        return animeDtoList;
    }

    @Override
    public AnimePage getAnimeByPage(Integer pageNo, Integer pageSize) {
        Pageable  pageable = PageRequest.of(pageNo, pageSize);
        Page<Anime> animePage = animeRepository.findAll(pageable);
        List<Anime> animeList = animePage.getContent();

        List<AnimeDto> animeDtoList = new ArrayList<>();

        for (Anime anime : animeList) {
            String posterUrl = baseUrl + "/file/" + anime.getPoster();
            AnimeDto animeDto = new AnimeDto(
                    anime.getAnimeId(),
                    anime.getTitle(),
                    anime.getDirector(),
                    anime.getStudio(),
                    anime.getAnimeCast(),
                    anime.getReleaseYear(),
                    anime.getPoster(),
                    posterUrl
            );
            animeDtoList.add(animeDto);
        }

        return new AnimePage(animeDtoList, pageNo, pageSize,
                            animePage.getTotalElements(),
                            animePage.getTotalPages(),
                            animePage.isLast());
    }

    @Override
    public AnimePage sortedAnimeByPage(Integer pageNo, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable  pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Anime> animePage = animeRepository.findAll(pageable);
        List<Anime> animeList = animePage.getContent();

        List<AnimeDto> animeDtoList = new ArrayList<>();

        for (Anime anime : animeList) {
            String posterUrl = baseUrl + "/file/" + anime.getPoster();
            AnimeDto animeDto = new AnimeDto(
                    anime.getAnimeId(),
                    anime.getTitle(),
                    anime.getDirector(),
                    anime.getStudio(),
                    anime.getAnimeCast(),
                    anime.getReleaseYear(),
                    anime.getPoster(),
                    posterUrl
            );
            animeDtoList.add(animeDto);
        }

        return new AnimePage(animeDtoList, pageNo, pageSize,
                animePage.getTotalElements(),
                animePage.getTotalPages(),
                animePage.isLast());
    }
}