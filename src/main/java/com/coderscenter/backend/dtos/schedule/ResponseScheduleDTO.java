package com.coderscenter.backend.dtos.schedule;

import com.coderscenter.backend.dtos.group.response.ResponseGroupWithListDTO;
import com.coderscenter.backend.dtos.week.ResponseWeekDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseScheduleDTO {

    private Long scheduleId;
    private ResponseGroupWithListDTO group;
    private List<ResponseWeekDTO> weeks;

}
