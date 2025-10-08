# 🎓 CodersCenter Backend

Ein modernes **Spring Boot Backend** für das CodersCenter Lernmanagementsystem mit umfassender Benutzer-, Mitarbeiter- und Studentenverwaltung, Terminplanung und automatisierter Stundenplanoptimierung.

## 📋 Inhaltsverzeichnis

- [Features](#-features)
- [Technologie-Stack](#️-technologie-stack)
- [Projekt-Struktur](#-projekt-struktur)
- [Installation & Setup](#-installation--setup)
- [API Dokumentation](#-api-dokumentation)
- [Deployment](#-deployment)
- [Datenbank](#-datenbank)
- [Sicherheit](#-sicherheit)
- [Testing](#-testing)
- [Contributing](#-contributing)

## ✨ Features

### 🔐 Authentifizierung & Autorisierung
- JWT-basierte Authentifizierung
- Rollenbasierte Zugriffskontrolle (Admin, Employee, Student)
- Sichere Passwort-Verschlüsselung
- First-Time Password Setup

### 👥 Benutzerverwaltung
- **User Management**: Vollständige CRUD-Operationen
- **Employee Management**: Mitarbeiterverwaltung mit Profilbildern
- **Student Management**: Studentenverwaltung mit Gruppenzuordnung
- **Profile Management**: Erweiterte Profilinformationen

### 📚 Kurs- & Gruppenverwaltung
- **Program Management**: Bildungsprogramme verwalten
- **Subject Management**: Fächer und Kursinhalte
- **Group Management**: Studentengruppen organisieren
- **Schedule Optimization**: Automatisierte Stundenplanoptimierung mit OptaPlanner

### 📊 Verwaltungstools
- **Attendance Tracking**: Anwesenheitsverfolgung
- **Contact Book**: Integriertes Kontaktbuch
- **Replacements**: Vertretungsplanung
- **Permission System**: Granulare Berechtigungsverwaltung

### 📧 Kommunikation
- **Email Integration**: Automatisierte E-Mail-Benachrichtigungen
- **Credential Generation**: Automatische Passwort-Generierung
- **Notification System**: Umfassendes Benachrichtigungssystem

## 🛠️ Technologie-Stack

### Backend Framework
- **Spring Boot 3.5.5** - Enterprise Java Framework
- **Spring Security** - Sicherheit und Authentifizierung
- **Spring Data JPA** - Datenzugriff und ORM
- **Hibernate** - ORM Framework

### Datenbank
- **PostgreSQL** - Primäre Produktionsdatenbank
- **H2** - In-Memory Datenbank für Tests
- **HikariCP** - Connection Pooling

### Dokumentation & API
- **OpenAPI 3** - API Dokumentation
- **Swagger UI** - Interaktive API-Dokumentation
- **Spring Boot Actuator** - Monitoring und Metrics

### Sicherheit & Authentifizierung
- **JWT (JSON Web Tokens)** - Token-basierte Authentifizierung
- **BCrypt** - Passwort-Hashing
- **CORS** - Cross-Origin Resource Sharing

### Optimierung & Tools
- **OptaPlanner** - Automatisierte Stundenplanoptimierung
- **Lombok** - Code-Generierung
- **Maven** - Dependency Management
- **Docker** - Containerisierung

### Testing
- **JUnit 5** - Unit Testing
- **Spring Boot Test** - Integration Testing
- **TestContainers** - Database Testing

## 📁 Projekt-Struktur

```
src/main/java/com/coderscenter/backend/
├── 🏗️ config/              # Konfiguration
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── WebConfig.java
├── 🎮 controller/           # REST Controllers
│   ├── AuthController.java
│   ├── UserController.java
│   ├── EmployeeController.java
│   └── StudentController.java
├── 📊 dtos/                 # Data Transfer Objects
│   ├── user/
│   ├── employee/
│   └── student/
├── 🗃️ entities/             # JPA Entities
│   ├── profile/
│   ├── group_management/
│   └── schedule_management/
├── 🔧 services/             # Business Logic
│   ├── UserService.java
│   ├── EmployeeService.java
│   └── helperService/
├── 🗂️ repositories/         # Data Access Layer
├── 🔀 mapper/               # Entity-DTO Mapping
├── ⚠️ exceptions/           # Custom Exceptions
├── 📈 enums/                # Enumerations
└── 🧩 optaplanner/          # OptaPlanner Configuration
```

## 🚀 Installation & Setup

### Voraussetzungen
- **Java 21+** (das Projekt verwendet Java 24)
- **Maven 3.8+**
- **Docker & Docker Compose** (für lokale Entwicklung)
- **PostgreSQL 14+** (oder Docker Container)

### 1. Repository klonen

```bash
git clone https://github.com/your-username/CodersCenter-Bend.git
cd CodersCenter-Bend
```

### 2. Umgebungsvariablen konfigurieren

Erstelle eine `.env` Datei:

```env
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/coders_center
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRATION=86400000

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://your-frontend.com
```

### 3. Mit Docker starten (Empfohlen)

```bash
# PostgreSQL mit Docker Compose starten
docker-compose up -d postgres

# Anwendung starten
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

### 4. Manueller Start

```bash
# Dependencies installieren
./mvnw clean install

# Tests ausführen
./mvnw test

# Anwendung starten
./mvnw spring-boot:run
```

Die Anwendung läuft auf: `http://localhost:8080`

## 📚 API Dokumentation

### Swagger UI
- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Wichtige Endpoints

#### Authentifizierung
```
POST /api/auth/login          # User Login
POST /api/auth/register       # User Registration
POST /api/auth/first-password # First-Time Password Setup
```

#### Benutzerverwaltung
```
GET    /api/users             # Alle Users abrufen
POST   /api/users             # Neuen User erstellen
GET    /api/users/{id}        # User Details
PUT    /api/users/{id}        # User aktualisieren
DELETE /api/users/{id}        # User löschen
```

#### Mitarbeiterverwaltung
```
GET    /api/employees         # Alle Mitarbeiter
POST   /api/employees         # Neuen Mitarbeiter erstellen
PUT    /api/employees/{id}    # Mitarbeiter aktualisieren
```

#### Studentenverwaltung
```
GET    /api/students          # Alle Studenten
POST   /api/students          # Neuen Student erstellen
PUT    /api/students/{id}     # Student aktualisieren
```

### Health Check
```
GET /actuator/health          # Anwendungsstatus
GET /actuator/info           # Anwendungsinfo
GET /actuator/metrics        # Application Metrics
```

## 🚀 Deployment

### Render.com (Empfohlen)

Das Projekt ist für Render.com mit Docker optimiert:

```bash
# 1. Repository auf GitHub pushen
git remote add origin https://github.com/your-username/CodersCenter-Bend.git
git push -u origin main

# 2. Render Blueprint verwenden
# Die render.yaml wird automatisch erkannt
```

Siehe [DEPLOYMENT.md](DEPLOYMENT.md) für detaillierte Anweisungen.

### Docker

```bash
# Image builden
docker build -t coders-center-backend .

# Container starten
docker run -p 8080:8080 \
  -e DATABASE_URL="your-db-url" \
  -e DATABASE_PASSWORD="your-password" \
  -e JWT_SECRET="your-secret" \
  coders-center-backend
```

## 🗄️ Datenbank

### Schema Übersicht

Das System verwendet folgende Hauptentitäten:

- **Users** - Basis-Benutzerdaten
- **Profiles** - Erweiterte Profilinformationen
- **Employees** - Mitarbeiterdaten
- **Students** - Studentendaten
- **Groups** - Studentengruppen
- **Programs** - Bildungsprogramme
- **Subjects** - Fächer/Kurse
- **Schedules** - Stundenplan-Management

### Mock Data

Das Projekt enthält Mock-Daten im `mockData/` Verzeichnis:

- `employees.csv` - Beispiel-Mitarbeiterdaten
- `students.csv` - Beispiel-Studentendaten
- `testusers.xlsx` - Test-Benutzerdaten

## 🔒 Sicherheit

### Authentifizierung
- **JWT Tokens** mit konfigurierbarer Expiration
- **BCrypt** Passwort-Hashing
- **CORS** Konfiguration für Frontend-Integration

### Autorisierung
- **Rollenbasierte Zugriffskontrolle**
- **Method-Level Security**
- **Granulare Berechtigungen**

### Best Practices
- Keine Passwörter in Logs
- Sichere Header-Konfiguration
- Input Validation
- SQL Injection Schutz durch JPA

## 🧪 Testing

```bash
# Alle Tests ausführen
./mvnw test

# Nur Unit Tests
./mvnw test -Dtest="*Test"

# Integration Tests
./mvnw test -Dtest="*IT"

# Test Coverage Report
./mvnw jacoco:report
```

### Test-Profile

```bash
# Mit H2 In-Memory Database
./mvnw spring-boot:run -Dspring-boot.run.profiles=test

# Mit PostgreSQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=deploy
```

## 📈 Monitoring

### Spring Boot Actuator Endpoints

```bash
# Application Health
curl http://localhost:8080/actuator/health

# System Metrics
curl http://localhost:8080/actuator/metrics

# Application Info
curl http://localhost:8080/actuator/info
```

### Logging

Konfigurierbare Log-Level in `application.properties`:

```properties
# Root Level
logging.level.root=INFO

# Application Specific
logging.level.com.coderscenter.backend=DEBUG

# Database Queries
logging.level.org.hibernate.SQL=DEBUG
```

## 🤝 Contributing

1. Fork das Repository
2. Erstelle einen Feature Branch (`git checkout -b feature/amazing-feature`)
3. Committe deine Änderungen (`git commit -m 'Add amazing feature'`)
4. Push den Branch (`git push origin feature/amazing-feature`)
5. Öffne einen Pull Request

### Code Standards
- **Java Code Style**: Google Java Format
- **Commit Messages**: Conventional Commits
- **Branch Naming**: feature/*, bugfix/*, hotfix/*

## 📄 Lizenz

Dieses Projekt steht unter der MIT License. Siehe [LICENSE](LICENSE) für Details.

## 👨‍💻 Entwickler

**Ettore Junior**
- GitHub: [@ettorejunior](https://github.com/ettorejunior)
- Email: ettore@coders-center.com

---

## 🔄 Changelog

### Version 1.0.0 (Current)
- ✅ Initial Release
- ✅ Complete User Management System
- ✅ Employee & Student Management
- ✅ JWT Authentication
- ✅ OptaPlanner Integration
- ✅ Docker Support
- ✅ Render.com Deployment Ready

---

**📞 Support**: Bei Fragen oder Problemen, erstelle bitte ein [Issue](https://github.com/your-username/CodersCenter-Bend/issues) im Repository.

**🌟 Star das Projekt**, wenn es dir hilft!