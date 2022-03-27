package com.querotattoo.controllers.exceptions;

import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFoundException e, WebRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardErrorMessage errorMessage = new StandardErrorMessage("Recurso não encontrado.", e.getMessage());
        return handleExceptionInternal(e, errorMessage, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<?> database(DatabaseException e, WebRequest request) {
        String error = "Database error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, request);
    }

    @ExceptionHandler(StandardError.class)
    public ResponseEntity<?> standardError(StandardError e, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardErrorMessage body = new StandardErrorMessage(e.getError(), e.getMessage());
        return handleExceptionInternal(new IllegalStateException(e.getMessage()), body, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<?> ExceptionMapperStandardImpl(RollbackException e, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(e, new StandardErrorMessage(status.getReasonPhrase(), e.getCause().getMessage()), new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = new StandardErrorMessage(status.getReasonPhrase(), ex.getMessage());
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> erros = ex.getBindingResult().getAllErrors();
        List<String> body = new ArrayList<>();
        erros.forEach(objectError -> body.add(objectError.getDefaultMessage()));
        StandardErrorMessage error = new StandardErrorMessage("Erro de Validação", body.toString());

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }
}
