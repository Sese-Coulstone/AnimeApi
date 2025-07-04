package com.aniflix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AnimeDto {

    private Integer animeId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Director cannot be blank")
    private String director;

    @NotBlank(message = "Studio cannot be blank")
    private String studio;

    private Set<String> animeCast;

    private Integer releaseYear;

    @NotBlank(message = "Poster cannot be blank")
    private String poster;

    @NotBlank(message = "Poster cannot be blank")
    private String posterUrl;
}
