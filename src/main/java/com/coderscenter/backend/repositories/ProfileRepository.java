package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findProfileById(Long userUserId);
}
