package com.popcornnight.popcornnight_backend.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private long id;
    private String title;
    private String description;
    private Date releaseDate;
    private int duration;
}
