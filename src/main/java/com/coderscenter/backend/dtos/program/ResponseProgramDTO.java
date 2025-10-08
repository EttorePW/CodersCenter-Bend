package com.coderscenter.backend.dtos.program;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseProgramDTO {

    private Long programId;
    private int duration;
    private String type;

}
