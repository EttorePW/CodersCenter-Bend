package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.permission.RequestPermissionDTO;
import com.coderscenter.backend.dtos.permission.ResponsePermissionDTO;
import com.coderscenter.backend.entities.profile.Permission;
import com.coderscenter.backend.enums.PermissionType;
import com.coderscenter.backend.exceptions.InvalidDateRangeException;
import com.coderscenter.backend.exceptions.InvalidPermissionTypeException;
import com.coderscenter.backend.exceptions.ResourceNotFoundException;
import com.coderscenter.backend.mapper.PermissionMapper;
import com.coderscenter.backend.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * Get all permissions from the database
     * @return list of {@link ResponsePermissionDTO}
     */
    public List<ResponsePermissionDTO> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toResponseDTO)
                .toList();
    }

    /**
     * Get permission by id
     * @param permissionId id of the permission
     * @return {@link ResponsePermissionDTO} of the permission
     * @throws ResourceNotFoundException if the permission is not found
     */
    public ResponsePermissionDTO getPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission mit der ID " + permissionId + " nicht gefunden."));
        return permissionMapper.toResponseDTO(permission);
    }

    /**
     * Create a new permission based on the provided DTO.
     *
     * @param permissionDTO DTO with permission details
     * @return {@link ResponsePermissionDTO} of the created permission
     * @throws InvalidPermissionTypeException if the permission name is not a valid {@link PermissionType}
     */
    public ResponsePermissionDTO createPermission(RequestPermissionDTO permissionDTO) {
        PermissionType type = parsePermissionType(permissionDTO.getName());

        // Validate date range if both dates are provided
        validateDateRange(permissionDTO.getStartDate(), permissionDTO.getEndDate());

        Permission permission = Permission.builder()
                .name(type)
                .description(permissionDTO.getDescription())
                .startDate(permissionDTO.getStartDate())
                .endDate(permissionDTO.getEndDate())
                .build();

        permissionRepository.save(permission);
        return permissionMapper.toResponseDTO(permission);
    }

    /**
     * Update an existing permission partially or fully.
     *
     * @param id ID of the permission to update
     * @param permissionDTO DTO with fields to update
     * @return {@link ResponsePermissionDTO} of the updated permission
     * @throws ResourceNotFoundException if permission with the given ID does not exist
     * @throws InvalidPermissionTypeException if the provided name is not a valid {@link PermissionType}
     */
    public ResponsePermissionDTO updatePermission(Long id, RequestPermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission mit ID " + id + " nicht gefunden"));

        if (permissionDTO.getName() != null) {
            permission.setName(parsePermissionType(permissionDTO.getName()));
        }
        if (permissionDTO.getStartDate() != null) {
            permission.setStartDate(permissionDTO.getStartDate());
        }
        if (permissionDTO.getEndDate() != null) {
            permission.setEndDate(permissionDTO.getEndDate());
        }
        if (permissionDTO.getDescription() != null) {
            permission.setDescription(permissionDTO.getDescription());
        }

        // Validate date range if both dates are provided
        validateDateRange(permission.getStartDate(), permission.getEndDate());

        permissionRepository.save(permission);
        return permissionMapper.toResponseDTO(permission);
    }

    /**
     * Delete a permission by its ID.
     *
     * @param id ID of the permission to delete
     * @throws ResourceNotFoundException if permission with the given ID does not exist
     */
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission mit ID " + id + " nicht gefunden"));
        permissionRepository.delete(permission);
    }

    /**
     * Helper method to safely parse PermissionType.
     * Throws {@link InvalidPermissionTypeException} if the name is invalid.
     */
    private PermissionType parsePermissionType(String name) {
        try {
            return PermissionType.valueOf(name);
        } catch (Exception e) {
            throw new InvalidPermissionTypeException("Ungültiger PermissionType: " + name);
        }
    }

    /**
     * Helper method to validate that the date range is valid.
     * Ensures endDate is not before startDate.
     *
     * @param startDate the start date of the permission
     * @param endDate   the end date of the permission
     * @throws InvalidDateRangeException if endDate is before startDate
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException("Enddatum darf nicht vor dem Startdatum liegen.");
        }
    }

}
