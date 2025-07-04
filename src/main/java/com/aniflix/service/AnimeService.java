package com.aniflix.service;

import com.aniflix.dto.AnimeDto;
import com.aniflix.dto.AnimePage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnimeService {
    AnimeDto addAnime(MultipartFile file, AnimeDto animeDto) throws IOException;

    AnimeDto getAnime(Integer id);

    AnimeDto updateAnime(Integer id, MultipartFile file, AnimeDto animeDto) throws IOException;

    void deleteAnime(Integer id) throws IOException;

    List<AnimeDto> getAllAnime();

    AnimePage getAnimeByPage(Integer pageNo, Integer pageSize);

    AnimePage sortedAnimeByPage(Integer pageNo, Integer pageSize, String sortBy, String dir);
}
