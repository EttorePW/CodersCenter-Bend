package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.profile.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeeByUser_Id(Long user_userId);
    
    // Note: For OptaPlanner, we use transactional loading in the service instead
    // to avoid MultipleBagFetchException
}
