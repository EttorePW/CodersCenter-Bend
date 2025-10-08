package com.coderscenter.backend.services;

import com.coderscenter.backend.components.EmailRespondGenerator;
import com.coderscenter.backend.dtos.user.request.*;
import com.coderscenter.backend.dtos.user.response.AuthDTO;
import com.coderscenter.backend.dtos.user.response.ResponseFirstPasswordSetDTO;
import com.coderscenter.backend.dtos.user.response.ResponseUserDTO;
import com.coderscenter.backend.entities.profile.Permission;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.exceptions.InvalidCredentialsException;
import com.coderscenter.backend.exceptions.PasswordMismatchException;
import com.coderscenter.backend.exceptions.UserAlreadyExistsException;
import com.coderscenter.backend.exceptions.UserNotFoundException;
import com.coderscenter.backend.mapper.UserMapper;
import com.coderscenter.backend.repositories.PermissionRepository;
import com.coderscenter.backend.repositories.UserRepository;
import com.coderscenter.backend.services.helperService.CredentialGeneratorService;
import com.coderscenter.backend.services.helperService.EmailSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CredentialGeneratorService credentialGeneratorService;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailSendingService emailSendingService;
    private final EmailRespondGenerator emailRespondGenerator;


    /**
     * Authenticate a user by username/email and password.
     * If authentication is successful, generate a JWT token and return an AuthDTO.
     *
     * @param loginDTO containing username/email and password
     * @return {@link AuthDTO} with user details and JWT token
     * @throws UserNotFoundException       if user is not found
     * @throws InvalidCredentialsException if credentials are invalid
     */
    public AuthDTO login(LoginDTO loginDTO) {
        String identifier = loginDTO.getUsername();

        // Find user by username or email
        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new UserNotFoundException("Benutzer konnte nicht gefunden werden"));

        try {
            // Authenticate always with username
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), loginDTO.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Ungültiger Benutzername oder Passwort!");
        }

        String jwt = tokenService.generateTokenWithClaims(user);
        return userMapper.toAuthDTO(user, jwt);
    }

    /**
     * Register a new user with the provided registration details.
     * If the email is already in use, throw UserAlreadyExistsException.
     * Generate a username and password automatically.
     * Save the new user to the database and return an AuthDTO with JWT token.
     *
     * @param registerDTO containing E-Mail and role
     * @return {@link AuthDTO} with user details and JWT token
     * @throws UserAlreadyExistsException if a user with the given email already exists
     */
    public AuthDTO register(UserRegisterRequestDTO registerDTO) {
        if (userRepository.findByUsernameOrEmail(registerDTO.getEmail(), registerDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Ein Benutzer mit dieser E-Mail existiert bereits!");
        }

        List<Permission> permissions = new ArrayList<>();
        String password = "Coders123";
        String username = credentialGeneratorService.autoGenerator("User");

        User user = new User(
                username,
                registerDTO.getEmail(),
                passwordEncoder.encode(password),
                registerDTO.getRole(),
                permissions);

        userRepository.save(user);

        String jwt = tokenService.generateTokenWithClaims(user);

        // E-Mails senden zur Registrierung
        try {
            String emailText = emailRespondGenerator.respondTextGenerator(user,password );
            emailSendingService.sendEmail(
                    user.getEmail(),
                    "Registrierungsbestätigung Coders Center",
                    emailText
            );
            System.out.println("Email sent successfully to: " + user.getEmail());
        } catch (Exception emailException) {
            System.err.println("Email sending failed but continuing: " + emailException.getMessage());
        }

        return userMapper.toAuthDTO(user, jwt);
    }

    /**
     * Set the password for a user who is logging in for the first time.
     * If the user has already logged in before, throw InvalidCredentialsException.
     * If the new password and confirm password do not match, throw PasswordMismatchException.
     * Update the user's password and set loggedInBefore to true.
     *
     * @param userId              ID of the user
     * @param firstPasswordSetDTO containing new password and confirmation
     * @return {@link ResponseFirstPasswordSetDTO} with status message
     * @throws UserNotFoundException       if user is not found
     * @throws InvalidCredentialsException if the password has already been set
     * @throws PasswordMismatchException   if the new password and confirmation do not match
     */
    public ResponseFirstPasswordSetDTO setFirstPassword(Long userId, FirstPasswordSetDTO firstPasswordSetDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit ID " + userId + " nicht gefunden"));

        if (user.isLoggedInBefore()) {
            throw new InvalidCredentialsException("Das Passwort wurde bereits gesetzt.");
        }

        if (!firstPasswordSetDTO.getNewPassword().equals(firstPasswordSetDTO.getConfirmPassword())) {
            throw new PasswordMismatchException("Die Passwörter stimmen nicht überein.");
        }

        user.setPassword(passwordEncoder.encode(firstPasswordSetDTO.getNewPassword()));
        user.setLoggedInBefore(true);
        userRepository.save(user);

        return ResponseFirstPasswordSetDTO.builder()
                .userId(user.getId())
                .loggedInBefore(true)
                .message("Passwort erfolgreich gesetzt.")
                .build();
    }

    /**
     * Update user details as an admin (email, password, role, permissions).
     * Only non-null fields in the UserAdminUpdateDTO will be updated.
     *
     * @param userId             ID of the user to update
     * @param userAdminUpdateDTO DTO containing fields to update
     * @return {@link ResponseUserDTO} with updated user details
     * @throws UserNotFoundException     if user is not found
     * @throws PasswordMismatchException if the new password and confirmPassword do not match
     */
    public ResponseUserDTO updateUser(Long userId, UserAdminUpdateDTO userAdminUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit ID " + userId + " nicht gefunden"));

        if (userAdminUpdateDTO.getEmail() != null) {
            user.setEmail(userAdminUpdateDTO.getEmail());
        }

        if (userAdminUpdateDTO.getPassword() != null) {
            if (!userAdminUpdateDTO.getPassword().equals(userAdminUpdateDTO.getConfirmPassword())) {
                throw new PasswordMismatchException("Die Passwörter stimmen nicht überein.");
            }
            user.setPassword(passwordEncoder.encode(userAdminUpdateDTO.getPassword()));
        }

        if (userAdminUpdateDTO.getRole() != null) {
            user.setRole(userAdminUpdateDTO.getRole());
        }

        if (userAdminUpdateDTO.getPermissions() != null) {
            List<Permission> permissions = permissionRepository.findAllById(userAdminUpdateDTO.getPermissions());
            user.setPermissions(permissions);
        }

        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    /**
     * Update user detail by the user themselves (email, password).
     * Only non-null fields in the UserSelfUpdateDTO will be updated.
     *
     * @param username username or email of the user to update
     * @param userSelfUpdateDTO DTO containing fields to update
     * @return {@link ResponseUserDTO} with updated user details
     * @throws UserNotFoundException     if user is not found
     * @throws PasswordMismatchException if the new password and confirmPassword do not match
     */
    public ResponseUserDTO updateSelf(String username, UserSelfUpdateDTO userSelfUpdateDTO) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit Benutzername/E-Mail " + username + " nicht gefunden"));

        if (userSelfUpdateDTO.getEmail() != null) {
            user.setEmail(userSelfUpdateDTO.getEmail());
        }

        if (userSelfUpdateDTO.getPassword() != null) {
            if (!userSelfUpdateDTO.getPassword().equals(userSelfUpdateDTO.getConfirmPassword())) {
                throw new PasswordMismatchException("Die Passwörter stimmen nicht überein.");
            }
            user.setPassword(passwordEncoder.encode(userSelfUpdateDTO.getPassword()));
        }
        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    /**
     * Get a list of all users in the system.
     *
     * @return List of {@link ResponseUserDTO} representing all users
     */
    public List<ResponseUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    /**
     * Get user details by user ID.
     * @param userId ID of the user to retrieve
     * @return {@link ResponseUserDTO} with user details
     * @throws UserNotFoundException if user is not found
     */
    public ResponseUserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit ID " + userId + " nicht gefunden"));

        return userMapper.toResponseDTO(user);
    }

    /**
     * Get user details by username.
     * @param username username or email of the user
     * @return {@link ResponseUserDTO} with user details
     * @throws UserNotFoundException if user is not found
     */
    public ResponseUserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit Username/E-Mail " + username + " nicht gefunden"));
        return userMapper.toResponseDTO(user);
    }

    /**
     * Delete a user by their ID.
     * @param userId ID of the user to delete
     * @throws UserNotFoundException if user is not found
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit ID " + userId + " nicht gefunden"));

        userRepository.delete(user);
    }

}
