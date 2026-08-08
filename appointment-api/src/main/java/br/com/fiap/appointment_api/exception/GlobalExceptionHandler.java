package br.com.fiap.appointment_api.exception;

import br.com.fiap.appointment_api.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> handleNotFound(

        ResourceNotFoundException exception,
        HttpServletRequest request
) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    request.getRequestURI()
            ));
}

@ExceptionHandler(BusinessException.class)
public ResponseEntity<ErrorResponse> handleBusiness(
        BusinessException exception,
        HttpServletRequest request
) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    request.getRequestURI()
            ));
}

@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGeneric(
        Exception exception,
        HttpServletRequest request
) {
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    "Erro interno do servidor.",
                    request.getRequestURI()
            ));
}
}