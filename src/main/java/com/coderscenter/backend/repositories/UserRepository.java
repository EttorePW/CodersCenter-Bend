package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);
    
    List<User> findByRole(Role role);
}
