package com.coderscenter.backend.dtos.week;

import com.coderscenter.backend.dtos.day.ResponseDayDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ResponseWeekDTO {

    private long weekId;
    private String label;
    private String weekStartDate;
    private Long schedule;
    private List<ResponseDayDTO> days;
}

