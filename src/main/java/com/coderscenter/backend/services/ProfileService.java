package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.profile.RequestProfileDTO;
import com.coderscenter.backend.dtos.profile.ResponseProfileDTO;
import com.coderscenter.backend.entities.profile.Profile;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.exceptions.ProfileNotFoundException;
import com.coderscenter.backend.exceptions.UserNotFoundException;
import com.coderscenter.backend.mapper.ProfileMapper;
import com.coderscenter.backend.repositories.ProfileRepository;
import com.coderscenter.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserRepository userRepository;

    /**
     * Get all profiles from the database
     * @return list of {@link ResponseProfileDTO}
     */
    public List<ResponseProfileDTO> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toResponseDTO)
                .toList();
    }

    /**
     * Get profile by user id
     * @param userId id of the user
     * @return {@link ResponseProfileDTO} with profile data
     * @throws UserNotFoundException if user with given id does not exist
     * @throws ProfileNotFoundException if profile for user with given id does not exist
     */
    public ResponseProfileDTO getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Benutzer mit der ID " + userId + " nicht gefunden"));
        Profile profile = profileRepository.findById(user.getProfile().getId())

                .orElseThrow(() -> new ProfileNotFoundException("Profil für Benutzer mit der ID " + userId + " nicht gefunden"));

        return profileMapper.toResponseDTO(profile);
    }

    /**
     * Update profile by profile id
     * Updates only fields that are not null in the request DTO
     * @param profileId id of the profile to update
     * @param requestProfileDTO DTO with updated profile data
     * @return {@link ResponseProfileDTO} with updated profile data
     * @throws ProfileNotFoundException if profile with given id does not exist
     */
    public ResponseProfileDTO updateProfile(Long profileId, RequestProfileDTO requestProfileDTO) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profil mit der ID " + profileId + " nicht gefunden"));

        if (requestProfileDTO.getFirstName() != null) {
            profile.setFirstName(requestProfileDTO.getFirstName());
        }
        if (requestProfileDTO.getLastName() != null) {
            profile.setLastName(requestProfileDTO.getLastName());
        }
        if (requestProfileDTO.getPhone() != null) {
            profile.setPhone(requestProfileDTO.getPhone());
        }
        if (requestProfileDTO.getBirthDate() != null) {
            profile.setBirthDate(requestProfileDTO.getBirthDate());
        }

        profileRepository.save(profile);
        return profileMapper.toResponseDTO(profile);
    }
}
