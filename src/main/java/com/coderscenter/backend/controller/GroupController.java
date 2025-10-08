package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.group.request.AssingDTO;
import com.coderscenter.backend.dtos.group.request.RequestGroupDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(groupService.getGroupById(id),HttpStatus.OK);
    }
    @GetMapping("/{id}/course")
    public ResponseEntity<?> getCoursesById(@PathVariable Long id) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(groupService.getCoursesById(id),HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> postNew(@RequestBody RequestGroupDTO requestGroupDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(groupService.createNew(requestGroupDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestGroupDTO requestGroupDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(groupService.updateGroup(id, requestGroupDTO),HttpStatus.CREATED);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AssingDTO assingDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(groupService.assignToGroup(id, assingDTO),HttpStatus.CREATED);
    }
    @PutMapping("/{groupId}/{employeeId}/remove-assign")
    public ResponseEntity<?> update(@PathVariable Long groupId,@PathVariable Long employeeId) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(groupService.removeAssignment(groupId, employeeId),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(groupService.deleteGroup(id),HttpStatus.OK);
    }

}
