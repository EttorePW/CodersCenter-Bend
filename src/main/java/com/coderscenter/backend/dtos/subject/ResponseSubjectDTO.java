package com.coderscenter.backend.dtos.subject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseSubjectDTO {

    private Long subjectId;
    private String name;

}
