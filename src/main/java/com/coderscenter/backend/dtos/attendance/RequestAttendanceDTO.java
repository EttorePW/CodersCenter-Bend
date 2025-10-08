package com.coderscenter.backend.dtos.attendance;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestAttendanceDTO {

    private boolean isPresent;
    private String reasonForAbsence;
    private String date;
    Long student;
}
