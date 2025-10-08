package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.user.response.AuthDTO;
import com.coderscenter.backend.dtos.user.response.ResponseUserDTO;
import com.coderscenter.backend.entities.profile.Permission;
import com.coderscenter.backend.entities.profile.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PermissionMapper permissionMapper;
    private final ProfileMapper profileMapper;

    public AuthDTO toAuthDTO(User user, String jwt) {

        List<String> permissions = new ArrayList<>();
        if (user.getPermissions() != null) {
            user.getPermissions().forEach((permission) -> {
                permissions.add(permission.getName().toString());
            });
        }

            return AuthDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().getLabel())
                    .permissions(permissions)
                    .jwt(jwt)
                    .loggedInBefore(user.isLoggedInBefore())
                    .build();
    }

    public ResponseUserDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        return ResponseUserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getLabel())
                .permissions(permissionMapper.toResponseDTOList(user.getPermissions()))
                .profile(user.getProfile() != null
                        ? profileMapper.toResponseDTO(user.getProfile())
                        : null)
                .build();
    }
}
