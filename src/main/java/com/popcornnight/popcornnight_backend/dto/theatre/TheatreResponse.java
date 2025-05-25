package com.popcornnight.popcornnight_backend.dto.theatre;

import java.util.List;

import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;

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
public class TheatreResponse {
    private long id;
    private String name;
    private String branch;
    private String location;
    private List<HallResponse> halls;
}
