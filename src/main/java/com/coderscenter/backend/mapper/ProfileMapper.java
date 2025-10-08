package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.profile.ResponseProfileDTO;
import com.coderscenter.backend.entities.profile.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileMapper {

    public ResponseProfileDTO toResponseDTO(Profile profile) {
        return ResponseProfileDTO.builder()
                .profileId(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhone())
                .birthDate(profile.getBirthDate())
                .profileImageUrl(
                        profile.getProfileImage() != null
                                ? "/uploads/" + profile.getProfileImage().getProfileImageUrl()
                                : "/uploads/default-profile.png"
                )
                .build();
    }
}
