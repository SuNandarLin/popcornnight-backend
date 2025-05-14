package com.popcornnight.popcornnight_backend.dto.theatre;

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
public class TheatreRequest {
    private String name;
    private String branch;
    private String location;
}
