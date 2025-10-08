package com.coderscenter.backend.dtos.attendance;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ResponseAttendanceDTO {

    private Long attendanceId;
    private boolean isPresent;
    private String reasonForAbsence;
    private String date;
    Long student;
}
