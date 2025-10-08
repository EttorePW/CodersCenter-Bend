package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.permission.ResponsePermissionDTO;
import com.coderscenter.backend.entities.profile.Permission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionMapper {

    public ResponsePermissionDTO toResponseDTO(Permission permission) {
        return ResponsePermissionDTO.builder()
                .permissionId(permission.getId())
                .name(permission.getName().toString())
                .startDate(permission.getStartDate())
                .endDate(permission.getEndDate())
                .description(permission.getDescription())
                .build();
    }

    public List<ResponsePermissionDTO> toResponseDTOList(List<Permission> permissions) {
        return permissions.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
