package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.profile.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
