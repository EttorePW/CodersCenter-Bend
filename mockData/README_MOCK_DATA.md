# Mock Data - Übersicht der Änderungen

## Zusammenfassung
Die Mock-Daten wurden erweitert und an die Anforderungen angepasst:

### ✅ Was geändert wurde:

1. **SVN-Nummern**: Alle auf **10 Stellen** erweitert und eindeutig gemacht
   - **Employees**: 1234567001 - 1234567020
   - **Students**: 2000000001 - 2000000020

2. **Datumsformat**: Alle Daten im Format **dd.MM.yyyy**
   - birthDate: z.B. `15.03.1995`
   - holidays: z.B. `14.02.2025,17.03.2025`
   - unavailableDates: z.B. `08.01.2025,22.01.2025`

3. **Employee-Daten erweitert** (`employees_enhanced.csv`):
   - **workDays**: z.B. `MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY`
   - **holidays**: Urlaubstage (0-3 pro Person)
   - **unavailableDates**: Krankheit/unverfügbare Tage (0-2 pro Person)
   - **subjects**: Fach-IDs die der Trainer unterrichten kann (1-5 Fächer)

## 📁 Dateien im mockData Ordner:

### Für Employees (CSV):
- `employees_enhanced.csv` ← **Neue erweiterte Datei**
- `employees.csv` ← Original (kann gelöscht werden)
- `empleados_import.csv` ← Original (kann gelöscht werden)

### Für Students (XLSX):
- `unique_users.xlsx` ← Deine bestehende Datei
- `students_template_for_excel.csv` ← **Vorlage für Excel-Import**

### JSON Dateien (falls gewünscht):
- `employees.json` ← Für direkten Import im Frontend
- `students.json` ← Für direkten Import im Frontend
- `subjects.json` ← Liste aller Fächer

## 🔧 Frontend Integration:

### Option 1: CSV/XLSX Dateien verwenden
```javascript
// Für CSV (employees)
import Papa from 'papaparse';

// Für XLSX (students) 
import * as XLSX from 'xlsx';
```

### Option 2: JSON Dateien verwenden
```javascript
import employeesData from '../mockData/employees.json';
import studentsData from '../mockData/students.json';
import subjectsData from '../mockData/subjects.json';
```

## 📋 Feldstruktur:

### Employee:
```
firstName, lastName, email, phone, birthDate (dd.MM.yyyy), svn (10 Stellen),
salary, address.street, address.city, address.zip, address.country,
subjects (comma-separated IDs), workDays (comma-separated), 
holidays (comma-separated dates), unavailableDates (comma-separated dates)
```

### Student:
```
firstName, lastName, email, phone, birthDate (dd.MM.yyyy), svn (10 Stellen),
amsOffice, address.street, address.city, address.zip, address.country
```

## 🎯 Nächste Schritte:

1. **Für Students XLSX**: 
   - Öffne `students_template_for_excel.csv` in Excel
   - Speichere als `students_updated.xlsx`
   - Ersetze die alte `unique_users.xlsx`

2. **Backend**: DataSetupRunner ist deaktiviert (Database bleibt leer)

3. **Frontend**: Importiere die Mock-Daten wie gewünscht

## 💡 Notizen:
- Alle SVN-Nummern sind eindeutig
- Deutsche Adressen für realistische Daten
- WorkDays verwenden englische Wochentags-Namen
- Subjects sind als Zahlen-IDs referenziert (1-15)
- Holidays und unavailableDates sind in 2025 für Tests