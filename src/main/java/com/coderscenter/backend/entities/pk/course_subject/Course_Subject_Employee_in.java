package com.coderscenter.backend.entities.pk.course_subject;

import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.profile.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course_Subject_Employee_in {


    @EmbeddedId
    private Course_Subject_Employee_in_PK id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("subjectId")
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Course_Subject_Employee_in(Group group, Subject subject, Employee employee) {
        this.group = group;
        this.subject = subject;
        this.employee = employee;
        this.id = new Course_Subject_Employee_in_PK(
                group.getGroupId(),
                subject.getSubjectId(),
                employee.getId()
        );
    }
    public Course_Subject_Employee_in() {}




}
