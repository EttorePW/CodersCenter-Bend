package com.coderscenter.backend.entities.group_management;



import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.profile.Student;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Groups")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String name;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @OneToMany
    @JoinColumn(name = "group_id")
    private List<Student> students;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course_Subject_Employee_in> courses = new ArrayList<>();

    public void addAssignment(Employee employee, Subject subject) {
        Course_Subject_Employee_in assignment = new Course_Subject_Employee_in(this, subject, employee);
        courses.add(assignment);
    }

    public void removeAssignment(Employee employee) {
        courses.removeIf(assignment -> assignment.getEmployee().equals(employee));
    }
}
