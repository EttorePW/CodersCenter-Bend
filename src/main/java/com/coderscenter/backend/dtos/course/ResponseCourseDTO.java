package com.coderscenter.backend.dtos.course;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseCourseDTO {

    private Long groupId;
    private List<ResponseSubjectWithEmployeeDTO> subjects;

}
