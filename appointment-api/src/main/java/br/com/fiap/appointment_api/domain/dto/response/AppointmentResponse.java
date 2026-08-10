package br.com.fiap.appointment_api.domain.dto.response;

import br.com.fiap.appointment_api.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse (
        Long id,
        PatientResponse patient,
        DoctorResponse doctor,
        LocalDateTime dateTime,
        AppointmentStatus status
) {
}