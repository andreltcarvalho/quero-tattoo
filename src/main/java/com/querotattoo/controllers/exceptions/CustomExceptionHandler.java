package com.querotattoo.controllers.exceptions;

import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorMessage> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardErrorMessage errorMessage = new StandardErrorMessage(error, e.getMessage(), request.getRequestURI());
        logErrorMessage(e);
        return ResponseEntity.status(status).body(errorMessage);
    }

    private void logErrorMessage(Throwable err) {
        logger.error("ERRO+", err);
    }

    @ExceptionHandler(StandardError.class)
    public ResponseEntity<StandardErrorMessage> illegalState(StandardError e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardErrorMessage errorMessage = new StandardErrorMessage(e.getError(), e.getMessage(), request.getRequestURI());
        logErrorMessage(e);
        return ResponseEntity.status(status).body(errorMessage);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        String error = "Database error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(status.value(), error, e.getMessage(), request.getRequestURI());
        logErrorMessage(err);
        return ResponseEntity.status(status).body(err);
    }
}
