package com.coderscenter.backend.dtos.program;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestProgramDTO {

    private int duration;
    private String type;
}
