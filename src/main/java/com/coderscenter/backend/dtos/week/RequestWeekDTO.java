package com.coderscenter.backend.dtos.week;

import com.coderscenter.backend.dtos.day.RequestDayDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class RequestWeekDTO {

    private String label;
    private String weekStartDate;
    private List<RequestDayDTO> days;

}
