package com.coderscenter.backend.dtos.schedule;

import com.coderscenter.backend.dtos.week.RequestWeekDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class RequestScheduleDTO {

    private Long groupId;
    private List<RequestWeekDTO> weeks;
}
