package com.aniflix.dto;

import java.util.List;

public record AnimePage(List<AnimeDto> animelist,
                        Integer pageNo,
                        Integer pageSize,
                        Long totalElements,
                        int totalPages,
                        boolean isLast)  {
}
