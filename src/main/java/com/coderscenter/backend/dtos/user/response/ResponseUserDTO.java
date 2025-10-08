package com.coderscenter.backend.dtos.user.response;

import com.coderscenter.backend.dtos.permission.ResponsePermissionDTO;
import com.coderscenter.backend.dtos.profile.ResponseProfileDTO;
import com.coderscenter.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseUserDTO {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private List<ResponsePermissionDTO> permissions;
    private ResponseProfileDTO profile;
}
