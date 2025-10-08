package com.coderscenter.backend.dtos.group.request;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Builder
@Data
public class AssingDTO {

    private HashMap<Long, List<Long>> subjectEmployee;
    private List<Long> students;

}
