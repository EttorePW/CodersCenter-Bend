package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.user.request.FirstPasswordSetDTO;
import com.coderscenter.backend.dtos.user.request.UserAdminUpdateDTO;
import com.coderscenter.backend.dtos.user.request.UserSelfUpdateDTO;
import com.coderscenter.backend.dtos.user.response.ResponseFirstPasswordSetDTO;
import com.coderscenter.backend.dtos.user.response.ResponseUserDTO;
import com.coderscenter.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // === Admin Endpoints ===

    /**
     * Get all users.
     *
     * @return list of {@link ResponseUserDTO}
     */
    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get user by ID.
     *
     * @param id ID of the user
     * @return {@link ResponseUserDTO} with user details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Update user details (admin only).
     *
     * @param id user ID
     * @param updateUserDTO fields to update
     * @return {@link ResponseUserDTO} with updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> update(
            @PathVariable Long id,
            @RequestBody UserAdminUpdateDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDTO));
    }

    /**
     * Delete user by ID.
     *
     * @param id ID of the user
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // === Self Endpoints ===

    /**
     * Get own user details.
     * @param authentication represents the currently authenticated user
     * @return {@link ResponseUserDTO} with own user details
     */
    @GetMapping("/self")
    public ResponseEntity<ResponseUserDTO> getSelf(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByUsername(authentication.getName()));
    }

    /**
     * Update own user details.
     * @param authentication represents the currently authenticated user
     * @param updateUserDTO fields to update
     * @return {@link ResponseUserDTO} with updated own user details
     */
    @PutMapping("/self")
    public ResponseEntity<ResponseUserDTO> updateSelf(Authentication authentication,
                                                      @RequestBody UserSelfUpdateDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateSelf(authentication.getName(), updateUserDTO));
    }

    // === Onboarding / First Password ===

    /**
     * Endpoint for setting the first password of a newly created user.
     * Can only be used once (before loggedInBefore is true).
     *
     * @param id  user ID
     * @param dto containing new password and confirmation
     * @return {@link ResponseFirstPasswordSetDTO} with status message
     */
    @PostMapping("/{id}/first-password")
    public ResponseEntity<ResponseFirstPasswordSetDTO> setFirstPassword(
            @PathVariable Long id,
            @RequestBody FirstPasswordSetDTO dto) {
        return ResponseEntity.ok(userService.setFirstPassword(id, dto));
    }
}
