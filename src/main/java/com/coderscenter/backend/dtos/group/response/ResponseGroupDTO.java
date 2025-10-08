package com.coderscenter.backend.dtos.group.response;

import com.coderscenter.backend.dtos.program.ResponseProgramDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
public class ResponseGroupDTO {

    private Long groupId;
    private String name;
    private String startDate;
    private String endDate;
    private boolean isActive;
    private ResponseProgramDTO program;

}
