package com.coderscenter.backend.dtos.day;

import com.coderscenter.backend.dtos.slot.ResponseSlotDTO;
import com.coderscenter.backend.enums.DayLabel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ResponseDayDTO {

    private Long dayId;
    private DayLabel label;
    private String dayDate;
    private Long weekId;
    private List<ResponseSlotDTO> slots;
}
