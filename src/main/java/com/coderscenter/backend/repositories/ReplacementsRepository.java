package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.management.Replacements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplacementsRepository extends JpaRepository<Replacements, Long> {

}
