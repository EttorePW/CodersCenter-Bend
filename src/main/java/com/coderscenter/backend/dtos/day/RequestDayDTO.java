package com.coderscenter.backend.dtos.day;

import com.coderscenter.backend.dtos.slot.RequestSlotDTO;
import com.coderscenter.backend.enums.DayLabel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class RequestDayDTO {

    private DayLabel label;
    private String dayDate;
    private List<RequestSlotDTO> slots;

}
