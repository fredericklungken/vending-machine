package com.data.vending.machine.controller;

import com.data.vending.machine.exception.DuplicateEntityException;
import com.data.vending.machine.exception.InvalidDenominationException;
import com.data.vending.machine.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidDenominationException.class)
    public ResponseEntity<String> handleInvalidDenomination(InvalidDenominationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<String> handleDuplicateEntity(DuplicateEntityException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Return 409 Conflict
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class) // Catch-all for any other exceptions
    public ResponseEntity<String> handleGeneralException(Exception e) {
        System.out.println("e" + e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String>validationCustomException(MethodArgumentNotValidException ex) {

        Map<String, List<String>> errors = new HashMap<>();
        errors.put("errorMessage", ex.getBindingResult()
                .getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));

        System.out.println("ex" + ex.getBindingResult());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("s"  + errors.entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                        .collect(Collectors.joining("\n")));

    }
}
