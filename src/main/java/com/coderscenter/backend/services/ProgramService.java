package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.program.RequestProgramDTO;
import com.coderscenter.backend.dtos.program.ResponseProgramDTO;
import com.coderscenter.backend.dtos.subject.RequestSubjectDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.group_management.Program;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.mapper.ProgramMapper;
import com.coderscenter.backend.repositories.GroupRepository;
import com.coderscenter.backend.repositories.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final GroupRepository groupRepository;

    public List<ResponseProgramDTO> getAllPrograms() {
        List<Program> programs = programRepository.findAll();
        List<ResponseProgramDTO> ResponseProgramDTOs = new ArrayList<>();
        programs.forEach(program -> {
            ResponseProgramDTOs.add(programMapper.toResponseDTO(program));
        });

        return ResponseProgramDTOs;
    }

    public ResponseProgramDTO getProgramById(long id) throws EmptyOptionalException {
        Optional<Program> program = programRepository.findById(id);
        if (program.isPresent()) {
            return programMapper.toResponseDTO(program.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/program/" + id);
    }

    public ResponseProgramDTO createNew(RequestProgramDTO requestProgramDTO) throws EmptyOptionalException {

        Program program = Program.builder()
                .duration(requestProgramDTO.getDuration())
                .type(requestProgramDTO.getType())

                .build();

        programRepository.save(program);

        return programMapper.toResponseDTO(program);
    }

    public ResponseProgramDTO updateProgram  ( long id, RequestProgramDTO requestProgramDTO) throws EmptyOptionalException {
        Optional<Program> program = programRepository.findById(id);
        if (program.isPresent()) {
            program.get().setDuration(requestProgramDTO.getDuration());
            program.get().setType(requestProgramDTO.getType());


            programRepository.save(program.get());
            return programMapper.toResponseDTO(program.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/program");
    }

    public String deleteProgram(long id) throws EmptyOptionalException {
        Optional<Program> program = programRepository.findById(id);
        if (program.isPresent()) {
            programRepository.delete(program.get());
            return "Delete Program with id " + id + " successfully!";
        }
        throw new EmptyOptionalException("Unexpected empty Optional, could not been deleted","/api/admin/program");
    }
}
