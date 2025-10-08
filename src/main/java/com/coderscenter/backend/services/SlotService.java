package com.coderscenter.backend.services;

import com.coderscenter.backend.components.EmailRespondGenerator;
import com.coderscenter.backend.dtos.slot.RequestSlotDTO;
import com.coderscenter.backend.dtos.slot.ResponseSlotDTO;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.schedule_management.Day;
import com.coderscenter.backend.entities.schedule_management.Slot;
import com.coderscenter.backend.entities.schedule_management.Week;
import com.coderscenter.backend.exceptions.SubjectNotMatchException;
import com.coderscenter.backend.mapper.SlotMapper;
import com.coderscenter.backend.repositories.DayRepository;
import com.coderscenter.backend.repositories.EmployeeRepository;
import com.coderscenter.backend.repositories.SlotRepository;
import com.coderscenter.backend.repositories.SubjectRepository;
import com.coderscenter.backend.services.helperService.DateParseService;
import com.coderscenter.backend.services.helperService.EmailSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final DayRepository dayRepository;
    private final SubjectRepository subjectRepository;
    private final EmployeeRepository employeeRepository;
    private final SlotMapper slotMapper;
    private final DateParseService dateParseService;

    private final EmailSendingService emailSendingService;
    private final EmailRespondGenerator emailRespondGenerator;

    public List<ResponseSlotDTO> getAllSlots() {
        List<ResponseSlotDTO> responseSlotDTOList = new ArrayList<>();
        List<Slot> slots = slotRepository.findAll();

        slots.forEach(slot -> {
            responseSlotDTOList.add(slotMapper.toResponseDTO(slot));
        });

        return responseSlotDTOList;
    }

    public ResponseSlotDTO getSlotById(Long id)  {

        Slot slot = slotRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Slot in der Datenbank gefunden!"));
        return slotMapper.toResponseDTO(slot);

    }

    public Slot createNew(RequestSlotDTO requestSlotDTO, Day day) {

        Subject subject = subjectRepository.findById(requestSlotDTO.getSubjectId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Subject in der Datenbank gefunden!"));
        Employee employee = employeeRepository.findById(requestSlotDTO.getEmployeeId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Employee in der Datenbank gefunden!"));
        if (!employee.getSubjects().contains(subject)) {

            throw new SubjectNotMatchException("Der Trainer Lehrt nicht den Fach:"+subject.getName(),"api/schedule/slot");

        } else {

            return Slot.builder()
                    .slotTopic(requestSlotDTO.getSlotTopic())
                    .startDate(dateParseService.stringToLocalDateTime(requestSlotDTO.getStartDate()))
                    .endDate(dateParseService.stringToLocalDateTime(requestSlotDTO.getEndDate()))
                    .day(day)
                    .subject(subject)
                    .employee(employee)
                    .build();

        }
    }

    public ResponseSlotDTO updateSlot(Long id, RequestSlotDTO requestSlotDTO) {

        Subject subject = subjectRepository.findById(requestSlotDTO.getSubjectId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Subject in der Datenbank gefunden!"));
        Employee employee = employeeRepository.findById(requestSlotDTO.getEmployeeId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Employee in der Datenbank gefunden!"));

        Slot slot = slotRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Slot in der Datenbank gefunden!"));
                slot.setSlotTopic(requestSlotDTO.getSlotTopic());
                slot.setStartDate(dateParseService.stringToLocalDateTime(requestSlotDTO.getStartDate()));
                slot.setEndDate(dateParseService.stringToLocalDateTime(requestSlotDTO.getEndDate()));
                slot.setSubject(subject);
                slot.setEmployee(employee);

        slotRepository.save(slot);

        // E-Mails senden nach Vertretungsregelung
        try {
            String emailText = emailRespondGenerator.replacementTextGenerator(slot,employee );
            emailSendingService.sendEmail(
                    employee.getEmail(),
                    "Einsatzplan Aktualisierung",
                    emailText
            );
            System.out.println("Email sent successfully to: " + employee.getEmail());
        } catch (Exception emailException) {
            System.err.println("Email sending failed but continuing: " + emailException.getMessage());
        }



        return slotMapper.toResponseDTO(slot);

    }

    public String deleteSlot(Long id) {
        slotRepository.deleteById(id);
        return "Slot " + id + " successfully deleted!";
    }
}
