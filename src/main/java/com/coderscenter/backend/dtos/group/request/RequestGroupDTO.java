package com.coderscenter.backend.dtos.group.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RequestGroupDTO {

    private String name;
    private String startDate;
    private String endDate;
    private boolean isActive;
    private Long programId;

}
