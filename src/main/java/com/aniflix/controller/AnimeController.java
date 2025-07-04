package com.aniflix.controller;

import com.aniflix.dto.AnimeDto;
import com.aniflix.exceptions.EmptyFileException;
import com.aniflix.service.AnimeService;
import com.aniflix.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/anime")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add-Anime")
    public ResponseEntity<AnimeDto> addAnime(@RequestPart MultipartFile file,
                                      @RequestPart String animeDto) throws IOException, EmptyFileException {
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty. Please upload a valid file.");
        }
        AnimeDto anime = convertToAnimeDto(animeDto);
        return new ResponseEntity<>(animeService.addAnime(file, anime), HttpStatus.CREATED);
    }
    @GetMapping("/get-Anime/{id}")
    public ResponseEntity<AnimeDto> getAnime(@PathVariable Integer id) {
        AnimeDto anime = animeService.getAnime(id);
        if (anime != null) {
            return new ResponseEntity<>(anime, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll-Anime")
    public List<AnimeDto> getAllAnime() {
        List<AnimeDto> anime = animeService.getAllAnime();
        return new ResponseEntity<>(anime, HttpStatus.OK).getBody();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAnime(@PathVariable Integer id) throws IOException {
        animeService.deleteAnime(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnimeDto> updateAnime(@PathVariable Integer id,
                                                @RequestPart MultipartFile file,
                                                @RequestPart String animeDto) throws IOException {
        AnimeDto anime = convertToAnimeDto(animeDto);
        return new ResponseEntity<>(animeService.updateAnime(id, file, anime), HttpStatus.OK);
    }

    @GetMapping("/getAnimeByPage")
    public ResponseEntity<?> getAnimeByPage(@RequestParam(defaultValue = AppConstants.PAGE_NO, required = false) Integer pageNo,
                                                   @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return ResponseEntity.ok(animeService.getAnimeByPage(pageNo, pageSize));
    }

    @GetMapping("/sortedAnimeByPage")
    public ResponseEntity<?> sortedAnimeByPage(@RequestParam(defaultValue = AppConstants.PAGE_NO, required = false) Integer pageNo,
                                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir) {
        return ResponseEntity.ok(animeService.sortedAnimeByPage(pageNo, pageSize, sortBy, dir));
    }

    private AnimeDto convertToAnimeDto(String animeDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(animeDto, AnimeDto.class);
    }
}
