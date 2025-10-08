package com.coderscenter.backend.dtos.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseFirstPasswordSetDTO {

    private Long userId;
    private boolean loggedInBefore;
    private String message;
}
