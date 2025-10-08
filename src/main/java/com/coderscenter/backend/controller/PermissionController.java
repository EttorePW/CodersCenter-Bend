package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.permission.RequestPermissionDTO;
import com.coderscenter.backend.dtos.permission.ResponsePermissionDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Get all permissions.
     *
     * @return list of {@link ResponsePermissionDTO}
     */
    @GetMapping
    public ResponseEntity<List<ResponsePermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    /**
     * Get a permission by its ID.
     *
     * @param permissionId ID of the permission
     * @return {@link ResponsePermissionDTO} with permission details
     */
    @GetMapping("/{permissionId}")
    public ResponseEntity<ResponsePermissionDTO> getPermissionById(@PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionService.getPermissionById(permissionId));
    }

    /**
     * Create a new permission.
     *
     * @param requestPermissionDTO DTO containing permission details
     * @return {@link ResponsePermissionDTO} of the created permission
     */
    @PostMapping
    public ResponseEntity<ResponsePermissionDTO> createPermission(
            @Valid @RequestBody RequestPermissionDTO requestPermissionDTO) {
        return new ResponseEntity<>(permissionService.createPermission(requestPermissionDTO), HttpStatus.CREATED);
    }

    /**
     * Update an existing permission.
     *
     * @param permissionId ID of the permission to update
     * @param requestPermissionDTO DTO containing updated fields
     * @return {@link ResponsePermissionDTO} of the updated permission
     */
    @PutMapping("/{permissionId}")
    public ResponseEntity<ResponsePermissionDTO> updatePermission(
            @PathVariable Long permissionId,
            @Valid @RequestBody RequestPermissionDTO requestPermissionDTO) {
        return ResponseEntity.ok(permissionService.updatePermission(permissionId, requestPermissionDTO));
    }

    /**
     * Delete a permission by ID.
     *
     * @param permissionId ID of the permission to delete
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}
