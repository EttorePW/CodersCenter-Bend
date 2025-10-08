package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.program.ResponseProgramDTO;
import com.coderscenter.backend.entities.group_management.Program;
import org.springframework.stereotype.Service;

@Service
public class ProgramMapper {

    public ResponseProgramDTO toResponseDTO(Program program) {

        return ResponseProgramDTO.builder()
                .programId(program.getProgramId())
                .duration(program.getDuration())
                .type(program.getType())
                .build();
    }

}
