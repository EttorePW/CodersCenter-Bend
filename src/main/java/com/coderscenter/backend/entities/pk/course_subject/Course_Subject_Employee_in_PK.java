package com.coderscenter.backend.entities.pk.course_subject;



import java.io.Serializable;
import java.util.Objects;

public class Course_Subject_Employee_in_PK implements Serializable {

    Long groupId;
    Long subjectId;
    Long employeeId;

    public Course_Subject_Employee_in_PK(Long groupId, Long subjectId, Long employeeId) {
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.employeeId = employeeId;
    }

    public Course_Subject_Employee_in_PK() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course_Subject_Employee_in_PK that)) return false;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(subjectId, that.subjectId) &&
                Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, subjectId, employeeId);
    }
}
