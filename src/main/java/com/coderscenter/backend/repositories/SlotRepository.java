package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.schedule_management.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
}
