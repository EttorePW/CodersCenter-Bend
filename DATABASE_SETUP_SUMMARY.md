# Database Migration to In-Memory H2 & Data Setup Summary

## Overview
Successfully migrated the backend database configuration from file-based H2 to in-memory H2 and implemented a comprehensive data setup script for OptaPlanner development and testing.

## Changes Made

### 1. Database Configuration Changes (`application.properties`)

**Before:**
```properties
spring.datasource.url=jdbc:h2:file:./data/CodersCenterDB
spring.jpa.hibernate.ddl-auto=update
```

**After:**
```properties
# H2 In-Memory Database (solo para testing o desarrollo)
spring.datasource.url=jdbc:h2:mem:CodersCenterDB;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### 2. OptaPlanner Configuration
- Temporarily disabled OptaPlanner autoconfiguration to focus on database setup:
```properties
spring.autoconfigure.exclude=org.optaplanner.spring.boot.autoconfigure.OptaPlannerAutoConfiguration
```

### 3. Data Setup Implementation

Created `DataSetupRunner.java` - A comprehensive CommandLineRunner that automatically populates the database with test data on application startup:

#### Data Generated:
- **25 Addresses** - Diverse German addresses across major cities
- **25 Users** - 20 trainers + 5 students with encrypted passwords and proper roles
- **15 Subjects** - Technical subjects like Java, Spring Boot, React, etc.
- **3 Programs** - Web Development, Java Enterprise, Full-Stack programs
- **20 Employees (Trainers)** with:
  - Random work days (3-5 days per week)
  - Holiday dates in the next 3 months
  - Unavailable dates (30% of trainers have some)
  - 2-5 random subjects each trainer can teach
  - Realistic salary ranges (45k-70k EUR)
- **20 Students** - Distributed across the 3 groups
- **3 Groups** - Each with assigned students and programs
- **3 Schedule structures** - Empty schedules ready for OptaPlanner

#### Key Features:
- **Deterministic Random Data**: Uses fixed seed (42) for consistent test data
- **Realistic Constraints**: Work days, holidays, unavailable dates for trainers
- **Proper Relationships**: Subjects assigned to trainers, students to groups, etc.
- **Database Cleanup**: Cleans existing data before creating new data
- **Detailed Logging**: Shows creation progress and final summary

## Benefits of In-Memory Database

### Advantages:
1. **Faster Development**: No file I/O operations, pure in-memory operations
2. **Clean Slate**: Database recreated on every application restart
3. **No File Management**: No need to clean up database files
4. **Perfect for Testing**: Ideal for development and testing OptaPlanner
5. **Consistent State**: Always starts with the same predictable data

### Trade-offs:
- Data is lost when application stops (intentional for development)
- Requires data setup script to populate (which we implemented)

## File Structure
```
backend/src/main/java/com/coderscenter/backend/
├── config/
│   └── DataSetupRunner.java          # New: Comprehensive data setup
└── resources/
    └── application.properties         # Modified: In-memory DB config
```

## Database Schema
The setup creates all necessary tables with proper relationships:
- Users, Employees, Students with authentication
- Groups, Programs, Subjects for course management
- Schedules, Weeks, Days, Slots for timetabling
- Junction tables for many-to-many relationships
- Additional tables for holidays, work days, unavailable dates

## Usage

### Starting the Application
```bash
./mvnw spring-boot:run
```

### Accessing H2 Console
- URL: http://localhost:8080/auth/h2
- JDBC URL: `jdbc:h2:mem:CodersCenterDB`
- Username: `sa`
- Password: (empty)

### Sample Data Available
After startup, you'll have:
- 20 trainers with diverse skills and availability
- 20 students in 3 different groups
- 15 subjects covering full-stack development
- 3 programs of different durations
- All necessary relationships properly established

## Next Steps

1. **Re-enable OptaPlanner**: Once data setup is verified
2. **Implement OptaPlanner Integration**: Use the prepared data structure
3. **Frontend Integration**: Connect React frontend with new data setup
4. **Testing**: Use consistent data for comprehensive testing

## Logging Output Example
```
=== Starte Dateninitialisierung für OptaPlanner ===
Bereinige bestehende Daten...
Erstelle Adressen...
Erstellt: 25 Adressen
Erstelle Benutzer...
Erstellt: 25 Benutzer
Erstelle Fächer...
Erstellt: 15 Fächer
...
=== TRAINER UND IHRE FÄCHER ===
Max Müller - Fächer: [Java Grundlagen, Spring Boot, React Entwicklung, Database Design] - Arbeitstage: [MON, TUE, WED, THU, FRI]
...
```

This setup provides a solid foundation for OptaPlanner development with realistic, consistent test data that's automatically available every time you start the application.