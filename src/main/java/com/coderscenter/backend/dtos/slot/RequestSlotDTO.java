package com.coderscenter.backend.dtos.slot;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RequestSlotDTO {

    private String slotTopic;
    private String startDate;
    private String endDate;
    private Long subjectId;
    private Long employeeId;

}
