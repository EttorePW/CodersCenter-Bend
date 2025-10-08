package com.coderscenter.backend.controller;


import com.coderscenter.backend.dtos.group.request.RequestGroupDTO;
import com.coderscenter.backend.dtos.slot.RequestSlotDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.mapper.SlotMapper;
import com.coderscenter.backend.services.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/schedule/slot")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;
    private final SlotMapper slotMapper;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(slotService.getAllSlots(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(slotService.getSlotById(id),HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<?> postNew(@RequestBody RequestSlotDTO requestSlotDTO) throws UsernameNotFoundException, EmptyOptionalException {
//        return new ResponseEntity<>(slotMapper.toResponseDTO(slotService.createNew(requestSlotDTO)), HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestSlotDTO requestSlotDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(slotService.updateSlot(id, requestSlotDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(slotService.deleteSlot(id),HttpStatus.OK);
    }
}
