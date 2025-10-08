package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.group_management.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
