package com.coderscenter.backend.dtos.slot;

import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder
@Data
public class ResponseSlotDTO {

    private long slotId;
    private String slotTopic;
    private String startDate;
    private String endDate;
    private Long dayId;
    private ResponseSubjectDTO subject;
    private ResponseEmployeeDTO employee;
}
