package com.coderscenter.backend.config;

import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.group_management.Program;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.profile.Address;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.profile.Student;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.entities.schedule_management.Schedule;
import com.coderscenter.backend.enums.Role;
import com.coderscenter.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.coderscenter.backend.enums.DayLabel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * Initialisiert die In-Memory-Datenbank mit Testdaten für OptaPlanner-Entwicklung.
 * Wird automatisch beim Anwendungsstart ausgeführt.
 */
// @Component - Deaktiviert für Frontend-Mock-Daten
@RequiredArgsConstructor
@Slf4j
public class DataSetupRunner implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ProgramRepository programRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Starte Dateninitialisierung für OptaPlanner ===");
        
        // Bestehende Daten bereinigen
        cleanupData();
        
        // Neue Testdaten erstellen
        createAddresses();
        createUsers();
        createSubjects();
        createPrograms();
        createEmployees();
        createStudents();
        createGroups();
        createSchedules();
        
        log.info("=== Dateninitialisierung erfolgreich abgeschlossen ===");
        logDataSummary();
    }

    private void cleanupData() {
        log.info("Bereinige bestehende Daten...");
        scheduleRepository.deleteAll();
        groupRepository.deleteAll();
        studentRepository.deleteAll();
        employeeRepository.deleteAll();
        subjectRepository.deleteAll();
        programRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
        log.info("Datenbereinigung abgeschlossen");
    }

    private List<Address> addresses = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private List<Program> programs = new ArrayList<>();

    private void createAddresses() {
        log.info("Erstelle Adressen...");
        
        String[] streets = {
            "Hauptstraße 123", "Bahnhofstraße 45", "Schulstraße 78", "Kirchgasse 12",
            "Marktplatz 34", "Ringstraße 56", "Gartenstraße 89", "Bergstraße 23",
            "Waldweg 67", "Seestraße 90", "Dorfstraße 15", "Lindenallee 42",
            "Rosenweg 73", "Eichenstraße 28", "Ahornweg 91", "Birkenstraße 37",
            "Tannenweg 64", "Fichtenstraße 19", "Buchenallee 82", "Kastanienstraße 46"
        };
        
        String[] cities = {
            "Berlin", "Hamburg", "München", "Köln", "Frankfurt", "Stuttgart",
            "Düsseldorf", "Leipzig", "Dortmund", "Essen", "Bremen", "Dresden",
            "Hannover", "Nürnberg", "Duisburg", "Bochum", "Wuppertal", "Bonn",
            "Bielefeld", "Mannheim"
        };
        
        String[] zipCodes = {
            "10115", "20095", "80331", "50667", "60311", "70173",
            "40213", "04109", "44135", "45127", "28195", "01067",
            "30159", "90403", "47051", "44787", "42103", "53113",
            "33602", "68159"
        };

        for (int i = 0; i < 25; i++) {
            Address address = Address.builder()
                .street(streets[i % streets.length])
                .city(cities[i % cities.length])
                .zip(zipCodes[i % zipCodes.length])
                .build();
            addresses.add(addressRepository.save(address));
        }
        
        log.info("Erstellt: {} Adressen", addresses.size());
    }

    private void createUsers() {
        log.info("Erstelle Benutzer...");
        
        String[] usernames = {
            "trainer.mueller", "trainer.schmidt", "trainer.weber", "trainer.wagner", "trainer.fischer",
            "trainer.becker", "trainer.schulz", "trainer.hoffmann", "trainer.koch", "trainer.richter",
            "trainer.klein", "trainer.wolf", "trainer.neumann", "trainer.schwarz", "trainer.braun",
            "trainer.krueger", "trainer.hartmann", "trainer.lange", "trainer.werner", "trainer.krause",
            "student.anna", "student.ben", "student.clara", "student.david", "student.emma"
        };

        for (int i = 0; i < usernames.length; i++) {
            User user = User.builder()
                .username(usernames[i])
                .email(usernames[i] + "@coderscenter.com")
                .password(passwordEncoder.encode("password123"))
                .role(i < 20 ? Role.TRAINER : Role.STUDENT)
                .enabled(true)
                .isLocked(false)
                .isExpired(false)
                .loggedInBefore(false)
                .build();
            users.add(userRepository.save(user));
        }
        
        log.info("Erstellt: {} Benutzer", users.size());
    }

    private void createSubjects() {
        log.info("Erstelle Fächer...");
        
        String[] subjectNames = {
            "Java Grundlagen",
            "Spring Boot",
            "React Entwicklung",
            "Database Design",
            "HTML & CSS",
            "JavaScript",
            "TypeScript",
            "Agile Methoden",
            "Git & Versionskontrolle",
            "Testing & QA",
            "DevOps Grundlagen",
            "REST APIs",
            "Microservices",
            "Cloud Computing",
            "Security Basics"
        };

        for (String name : subjectNames) {
            Subject subject = Subject.builder()
                .name(name)
                .build();
            subjects.add(subjectRepository.save(subject));
        }
        
        log.info("Erstellt: {} Fächer", subjects.size());
    }

    private void createPrograms() {
        log.info("Erstelle Programme...");
        
        Program webDev = Program.builder()
            .type("Web Development Bootcamp")
            .duration(12)
            .build();
        programs.add(programRepository.save(webDev));
        
        Program javaDev = Program.builder()
            .type("Java Enterprise Development")
            .duration(16)
            .build();
        programs.add(programRepository.save(javaDev));
        
        Program fullStack = Program.builder()
            .type("Full-Stack Developer Program")
            .duration(20)
            .build();
        programs.add(programRepository.save(fullStack));
        
        log.info("Erstellt: {} Programme", programs.size());
    }

    private void createEmployees() {
        log.info("Erstelle Trainer (Employees)...");
        
        String[] firstNames = {
            "Max", "Anna", "Peter", "Sarah", "Michael", "Lisa", "Thomas", "Julia",
            "Christian", "Nicole", "Andreas", "Katharina", "Stefan", "Melanie", "Daniel",
            "Sabine", "Markus", "Claudia", "Oliver", "Petra"
        };
        
        String[] lastNames = {
            "Müller", "Schmidt", "Weber", "Wagner", "Fischer", "Becker", "Schulz", "Hoffmann",
            "Koch", "Richter", "Klein", "Wolf", "Neumann", "Schwarz", "Braun",
            "Krüger", "Hartmann", "Lange", "Werner", "Krause"
        };

        Random random = new Random(42); // Fixed seed für konsistente Daten
        
        for (int i = 0; i < 20; i++) {
            // Arbetstage zufällig zuweisen (mindestens 3, maximal 5 Tage)
            Set<DayLabel> workDays = generateRandomWorkDays(random);
            
            // Zufällige Urlaubstage in den nächsten 3 Monaten
            Set<LocalDate> holidays = generateRandomHolidays(random);
            
            // Einige Trainer haben zufällige unverfügbare Tage
            Set<LocalDate> unavailableDates = new HashSet<>();
            if (random.nextFloat() < 0.3) { // 30% haben unverfügbare Tage
                unavailableDates = generateRandomUnavailableDates(random);
            }
            
            Employee employee = Employee.builder()
                .firstName(firstNames[i])
                .lastName(lastNames[i])
                .email(firstNames[i].toLowerCase() + "." + lastNames[i].toLowerCase() + "@coderscenter.com")
                .phone("+49 " + (30 + random.nextInt(70)) + " " + (10000000 + random.nextInt(89999999)))
                .birthDate(LocalDate.of(1980 + random.nextInt(25), 1 + random.nextInt(12), 1 + random.nextInt(28)))
                .svn("T" + String.format("%04d", 1000 + i))
                .address(addresses.get(i))
                .salary(45000.0 + random.nextInt(25000))
                .user(users.get(i))
                .subjects(assignRandomSubjects(random))
                .workDays(workDays)
                .holidays(holidays)
                .unavailableDates(unavailableDates)
                .build();
            
            employeeRepository.save(employee);
        }
        
        log.info("Erstellt: 20 Trainer mit verschiedenen Fähigkeiten und Verfügbarkeiten");
    }

    private Set<DayLabel> generateRandomWorkDays(Random random) {
        Set<DayLabel> workDays = new HashSet<>();
        DayLabel[] allDays = {DayLabel.MONTAG, DayLabel.DIENSTAG, DayLabel.MITTWOCH, 
                              DayLabel.DONNERSTAG, DayLabel.FREITAG};
        
        // Mindestens 3, maximal 5 Arbeitstage
        int numWorkDays = 3 + random.nextInt(3);
        List<DayLabel> availableDays = new ArrayList<>(Arrays.asList(allDays));
        
        for (int i = 0; i < numWorkDays; i++) {
            int index = random.nextInt(availableDays.size());
            workDays.add(availableDays.remove(index));
        }
        
        return workDays;
    }

    private Set<LocalDate> generateRandomHolidays(Random random) {
        Set<LocalDate> holidays = new HashSet<>();
        LocalDate today = LocalDate.now();
        
        // 0-3 Urlaubstage in den nächsten 3 Monaten
        int numHolidays = random.nextInt(4);
        for (int i = 0; i < numHolidays; i++) {
            LocalDate holiday = today.plusDays(random.nextInt(90));
            holidays.add(holiday);
        }
        
        return holidays;
    }

    private Set<LocalDate> generateRandomUnavailableDates(Random random) {
        Set<LocalDate> unavailableDates = new HashSet<>();
        LocalDate today = LocalDate.now();
        
        // 1-2 unverfügbare Tage in den nächsten 2 Wochen
        int numUnavailable = 1 + random.nextInt(2);
        for (int i = 0; i < numUnavailable; i++) {
            LocalDate unavailable = today.plusDays(random.nextInt(14));
            unavailableDates.add(unavailable);
        }
        
        return unavailableDates;
    }

    private List<Subject> assignRandomSubjects(Random random) {
        List<Subject> assignedSubjects = new ArrayList<>();
        
        // Jeder Trainer kann 2-5 Fächer unterrichten
        int numSubjects = 2 + random.nextInt(4);
        List<Subject> availableSubjects = new ArrayList<>(subjects);
        Collections.shuffle(availableSubjects, random);
        
        for (int i = 0; i < Math.min(numSubjects, availableSubjects.size()); i++) {
            assignedSubjects.add(availableSubjects.get(i));
        }
        
        return assignedSubjects;
    }

    private void createStudents() {
        log.info("Erstelle Studenten...");
        
        String[] studentFirstNames = {
            "Anna", "Ben", "Clara", "David", "Emma", "Felix", "Greta", "Hans",
            "Ida", "Jonas", "Klara", "Leon", "Mia", "Noah", "Olivia", "Paul",
            "Quinn", "Rosa", "Sam", "Tina"
        };
        
        String[] studentLastNames = {
            "Andersson", "Bernstein", "Carlsson", "Dietrich", "Engel", "Friedrich", 
            "Gross", "Hagen", "Ingwer", "Jansen", "Keller", "Lindberg", "Meier",
            "Nordmann", "Olsen", "Petersen", "Quandt", "Rosen", "Sommer", "Thieme"
        };

        Random random = new Random(42);
        
        for (int i = 0; i < 20; i++) {
            Student student = Student.builder()
                .firstName(studentFirstNames[i])
                .lastName(studentLastNames[i])
                .email(studentFirstNames[i].toLowerCase() + "." + studentLastNames[i].toLowerCase() + "@student.com")
                .phone("+49 " + (160 + random.nextInt(20)) + " " + (1000000 + random.nextInt(8999999)))
                .birthDate(LocalDate.of(1995 + random.nextInt(10), 1 + random.nextInt(12), 1 + random.nextInt(28)))
                .svn("S" + String.format("%04d", 2000 + i))
                .address(null) // Students don't need addresses for this test setup
                .amsOffice("AMS Wien " + (i % 3 + 1))
                .user(users.size() > 20 + i ? users.get(20 + i) : null) // Assign individual student users
                .build();
            
            studentRepository.save(student);
        }
        
        log.info("Erstellt: 20 Studenten");
    }

    private void createGroups() {
        log.info("Erstelle Gruppen mit Trainer- und Studentenzuweisungen...");
        
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Student> allStudents = studentRepository.findAll();
        
        // Gruppe 1: Web Development Bootcamp
        Group webGroup = Group.builder()
            .name("Web-Dev-2024-A")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusWeeks(12))
            .isActive(true)
            .program(programs.get(0))
            .students(allStudents.subList(0, 7))
            .build();
        
        Group savedWebGroup = groupRepository.save(webGroup);
        
        // Studenten der Gruppe zuweisen
        for (Student student : allStudents.subList(0, 7)) {
            student.setGroup(savedWebGroup);
            studentRepository.save(student);
        }
        
        // Gruppe 2: Java Enterprise Development
        Group javaGroup = Group.builder()
            .name("Java-Enterprise-2024-B")
            .startDate(LocalDate.now().plusWeeks(2))
            .endDate(LocalDate.now().plusWeeks(18))
            .isActive(true)
            .program(programs.get(1))
            .students(allStudents.subList(7, 13))
            .build();
        
        Group savedJavaGroup = groupRepository.save(javaGroup);
        
        // Studenten der Gruppe zuweisen
        for (Student student : allStudents.subList(7, 13)) {
            student.setGroup(savedJavaGroup);
            studentRepository.save(student);
        }
        
        // Gruppe 3: Full-Stack Developer Program
        Group fullStackGroup = Group.builder()
            .name("FullStack-2024-C")
            .startDate(LocalDate.now().plusWeeks(1))
            .endDate(LocalDate.now().plusWeeks(21))
            .isActive(true)
            .program(programs.get(2))
            .students(allStudents.subList(13, 20))
            .build();
        
        Group savedFullStackGroup = groupRepository.save(fullStackGroup);
        
        // Studenten der Gruppe zuweisen
        for (Student student : allStudents.subList(13, 20)) {
            student.setGroup(savedFullStackGroup);
            studentRepository.save(student);
        }
        
        log.info("Erstellt: 3 Gruppen mit insgesamt 20 Studenten");
    }

    private void createSchedules() {
        log.info("Erstelle leere Stundenplan-Strukturen für OptaPlanner...");
        
        List<Group> allGroups = groupRepository.findAll();
        
        for (Group group : allGroups) {
            Schedule schedule = Schedule.builder()
                .group(group)
                .weeks(new ArrayList<>()) // Vorerst leer - wird von OptaPlanner gefüllt
                .build();
            
            scheduleRepository.save(schedule);
        }
        
        log.info("Erstellt: {} leere Stundenpläne", allGroups.size());
    }

    private void logDataSummary() {
        log.info("\n" +
                "=== DATENBANK ZUSAMMENFASSUNG ===\n" +
                "Adressen: {}\n" +
                "Benutzer: {}\n" +
                "Fächer: {}\n" +
                "Programme: {}\n" +
                "Trainer: {}\n" +
                "Studenten: {}\n" +
                "Gruppen: {}\n" +
                "Stundenpläne: {}\n" +
                "=================================",
                addressRepository.count(),
                userRepository.count(),
                subjectRepository.count(),
                programRepository.count(),
                employeeRepository.count(),
                studentRepository.count(),
                groupRepository.count(),
                scheduleRepository.count()
        );
        
        // Zeige Trainer-Fach-Zuordnungen
        log.info("\n=== TRAINER UND IHRE FÄCHER ===");
        employeeRepository.findAll().forEach(employee -> {
            String subjectNames = employee.getSubjects().stream()
                .map(Subject::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Keine Fächer");
            
            String workDayNames = employee.getWorkDays().stream()
                .map(day -> day.toString().substring(0, 3))
                .reduce((a, b) -> a + ", " + b)
                .orElse("Keine Arbeitstage");
                
            log.info("{} {} - Fächer: [{}] - Arbeitstage: [{}]", 
                employee.getFirstName(), 
                employee.getLastName(),
                subjectNames,
                workDayNames);
        });
        
        log.info("===============================\n");
    }
}