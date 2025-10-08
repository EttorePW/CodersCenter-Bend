package com.coderscenter.backend.entities.group_management;

import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Subject")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "subject")
    private List<Course_Subject_Employee_in> courses = new ArrayList<>();

}
