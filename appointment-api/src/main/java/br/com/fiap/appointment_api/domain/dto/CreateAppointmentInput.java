package br.com.fiap.appointment_api.domain.dto;

import java.time.LocalDateTime;

public record CreateAppointmentInput (
        Long patientId,
        Long doctorId,
        LocalDateTime dateTime
) {
}