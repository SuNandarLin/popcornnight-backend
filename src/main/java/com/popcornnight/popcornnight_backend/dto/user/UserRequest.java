package com.popcornnight.popcornnight_backend.dto.user;

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
public class UserRequest {
    private Boolean isAnonymous;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String role;
}
