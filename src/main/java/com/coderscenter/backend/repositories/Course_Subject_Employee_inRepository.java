package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in;
import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in_PK;
import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.group_management.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Course_Subject_Employee_inRepository extends JpaRepository<Course_Subject_Employee_in, Course_Subject_Employee_in_PK> {
    
    /**
     * Findet alle Trainerzuweisungen für eine bestimmte Gruppe und ein Fach
     * @param group Die Gruppe
     * @param subject Das Fach
     * @return Liste der zugewiesenen Trainer
     */
    List<Course_Subject_Employee_in> findByGroupAndSubject(Group group, Subject subject);

}
