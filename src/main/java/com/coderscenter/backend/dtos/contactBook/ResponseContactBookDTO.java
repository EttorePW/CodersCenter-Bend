package com.coderscenter.backend.dtos.contactBook;

import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.enums.ApplicationChannel;
import com.coderscenter.backend.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ResponseContactBookDTO {

    private Long contactBookId;
    private String companyName;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private ApplicationChannel applicationChannel;
    private ApplicationStatus applicationStatus;
    private String adPath;
    private ResponseStudentDTO student;
    private LocalDate createdAt;
}
