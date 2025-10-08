package com.coderscenter.backend.dtos.user.response;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthDTO {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private List<String> permissions;
    private String jwt;
    private boolean loggedInBefore;
}
