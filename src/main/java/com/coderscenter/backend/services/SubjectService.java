package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.permission.RequestPermissionDTO;
import com.coderscenter.backend.dtos.permission.ResponsePermissionDTO;
import com.coderscenter.backend.dtos.subject.RequestSubjectDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.profile.Permission;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.mapper.SubjectMapper;
import com.coderscenter.backend.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;


    public List<ResponseSubjectDTO> getAllPermission() {
        List<Subject> subjects = subjectRepository.findAll();
        List<ResponseSubjectDTO> responseSubjectDTOS = new ArrayList<>();
        subjects.forEach(subject -> {
            responseSubjectDTOS.add(subjectMapper.toResponseDTO(subject));
        });

        return responseSubjectDTOS;
    }

    public ResponseSubjectDTO getPermissionById(long id) throws EmptyOptionalException {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            return subjectMapper.toResponseDTO(subject.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/subject/" + id);
    }

    public ResponseSubjectDTO createNew(RequestSubjectDTO subjectDTO) throws EmptyOptionalException {

        Subject subject = Subject.builder()
                .name(subjectDTO.getName()).build();

        subjectRepository.save(subject);

        return subjectMapper.toResponseDTO(subject);
    }

    public ResponseSubjectDTO updateSubject  ( long id, RequestSubjectDTO subjectDTO) throws EmptyOptionalException {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            subject.get().setName(subjectDTO.getName());

            subjectRepository.save(subject.get());
            return subjectMapper.toResponseDTO(subject.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/permission");
    }

    public String deleteSubject(long id) throws EmptyOptionalException {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            subjectRepository.delete(subject.get());
            return "Delete Subject with id " + id + " successfully!";
        }
        throw new EmptyOptionalException("Unexpected empty Optional, could not been deleted","/api/admin/permission");
    }



}
