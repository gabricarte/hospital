package br.com.fiap.notification_api.domain.dto;

import java.time.LocalDateTime;

public record AppointmentNotificationDTO(
        String patientEmail,
        String patientName,
        String doctorName,
        LocalDateTime appointmentDateTime
) {}
