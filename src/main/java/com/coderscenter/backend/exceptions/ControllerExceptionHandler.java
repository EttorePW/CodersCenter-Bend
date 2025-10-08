package com.coderscenter.backend.exceptions;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Interner Serverfehler");
        problemDetail.setDetail("Ein unerwarteter Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.");
        problemDetail.setType(URI.create("urn:problem-type:internal-server-error"));
        return problemDetail;
    }

    @ExceptionHandler(SubjectNotMatchException.class)
    public ProblemDetail subjectNotMatchException(SubjectNotMatchException e) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setInstance(URI.create(e.getUrl()));
        problem.setTitle("Subject Not Match");
        problem.setDetail(e.getMessage());
        problem.setStatus(HttpStatus.BAD_REQUEST);
        problem.setProperty("TimeStamp", Instant.now().toString());
        return problem;
    }

    @ExceptionHandler(ThereAreStudentsAssigned.class)
    public ProblemDetail thereAreStudentsAssigned(ThereAreStudentsAssigned e) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setInstance(URI.create(e.getUrl()));
        problem.setTitle("Students are assigned");
        problem.setDetail(e.getMessage());
        problem.setStatus(HttpStatus.BAD_REQUEST);
        problem.setProperty("TimeStamp", Instant.now().toString());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("Some fields are invalid. See 'errors' for details.");
        problemDetail.setType(URI.create("urn:problem-type:validation"));

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Benutzer nicht gefunden");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:user-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Benutzer existiert bereits");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:user-already-exists"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Ungültige Anmeldeinformationen");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:invalid-credentials"));
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Ressource nicht gefunden");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:resource-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ProblemDetail handleStudentNotFound(StudentNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Student nicht gefunden");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:student-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleEmployeeNotFound(EmployeeNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Mitarbeiter nicht gefunden");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:employee-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ProblemDetail handlePasswordMismatch(PasswordMismatchException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Passwörter stimmen nicht überein");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:password-mismatch"));
        return problemDetail;
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ProblemDetail handleProfileNotFound(ProfileNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Profil nicht gefunden");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:profile-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ProblemDetail handleInvalidFileType(InvalidFileTypeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Ungültiger Dateityp");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:invalid-file-type"));
        return problemDetail;
    }

    @ExceptionHandler(IOException.class)
    public ProblemDetail handleIOException(IOException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Dateisystemfehler");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:file-storage"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidPermissionTypeException.class)
    public ProblemDetail handleInvalidPermissionType(InvalidPermissionTypeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Ungültiger PermissionType");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:invalid-permission-type"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ProblemDetail handleInvalidDateRange(InvalidDateRangeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Ungültiger Datumsbereich");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:invalid-date-range"));
        return problemDetail;
    }

}
