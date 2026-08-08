package br.com.fiap.appointment_api.exception.response;

import java.time.LocalDateTime;

public record ErrorResponse (

        LocalDateTime timestamp,
        String message,
        String path
) {
}