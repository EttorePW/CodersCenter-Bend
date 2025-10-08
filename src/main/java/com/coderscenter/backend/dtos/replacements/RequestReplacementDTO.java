package com.coderscenter.backend.dtos.replacements;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestReplacementDTO {

    private String employeeId;
    private String employeeName;
    private String slotId;
    private String subjectName;
    private String startDate;
    private String endDate;
    private String reason;

}
