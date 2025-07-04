package com.aniflix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer animeId;

    @Column(nullable = false)
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Director cannot be blank")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Studio cannot be blank")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "anime_cast")
    private Set<String> animeCast;

    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Poster cannot be blank")
    private String poster;

}
